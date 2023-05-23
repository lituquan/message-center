package com.center.message.core;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.center.message.enums.MessageType;
import com.center.message.expression.ExpressionHandler;
import com.center.message.expression.ExpressionHandlerFactory;
import com.center.message.mock.sender.Sender;
import com.center.message.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractMessageHandler implements ApplicationListener<MessageEvent> {
    @Autowired
    private ProhibitionService prohibitionService;
    @Autowired
    private MessagePathService messagePathService;
    @Autowired
    private MessageJobService messageJobService;
    @Autowired
    private MessageLogService messageLogService;
    @Autowired
    ExpressionHandlerFactory expressionHandlerFactory;

    @Async("normalThreadPool")
    @Override
    public void onApplicationEvent(MessageEvent event) {
        try {
            handle(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handle(MessageEvent event) {
        log.info("start to send {}, event: [{}]", messageType(), JSONUtil.toJsonStr(event));
        MessageBody messageBody = event.getMessageBody();
        //1.1.校验入参
        if (!isValid(messageBody)) {
            log.warn("消息不合法");
            return;
        }
        //1.3.过滤
        List<User> userList = messageBody.getUsers().stream().filter(s -> filter(s)).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(userList)) {
            log.warn("onApplicationEvent, none msg needs to be send.");
            return;
        }
        messageBody.setUsers(userList);
        messageBody.setMessageType(messageType());
        //1.4.检测是否为禁发消息
        Boolean b = prohibitionService.checkProhibition(messageBody);
        if (b) {
            log.info("onApplicationEvent, but this msg is prohibited by rules.");
            return;
        }
        //2.获取已开启、可用的场景配置、模板信息
        List<MessagePath> finalPathDtoList = findSceneAndTemplate(messageBody);
        if (CollectionUtil.isEmpty(finalPathDtoList)) {
            log.info("onApplicationEvent, but this scene is not exist or opened.");
            return;
        }
        for (MessagePath path : finalPathDtoList) {
            //2.1获取入参字段，拼装发送消息服务的入参
            Map<String, Object> paramMap = getParam(messageBody.getParam());
            handleParam(paramMap, path);
            //2.2是否定时
            if (path.isCorn()) {
                MessageJob job = MessageJob.builder()
                        .messageBody(messageBody)
                        .path(path)
                        .param(paramMap)
                        .build();
                messageJobService.saveJob(job);
                continue;
            }
            //2.3不是定时消息，组装参数，调用消息服务发送出去，保存记录
            String sn = UUID.randomUUID().toString();
            path.setMessageId(sn);
            path.setParam(paramMap);
            //2.5发送
            sendMessage(path, userList);
        }
    }

    public void sendMessage(MessagePath path, List<User> userList) {
        Sender sender = SenderFactory.findSender(messageType());
        log.info("sender is:{}", sender.getClass().getName());
        userList.forEach(user -> {
            sender.send(user, path.getTemplate());
        });
    }

    // 处理参数
    public Map<String, Object> getParam(JSONObject othersMap) {
        Map<String, Object> paramMap = new HashMap<>(8);
        for (String parameterName : othersMap.keySet()) {
            String value = StringUtils.isEmpty(othersMap.getStr(parameterName)) ? "" : othersMap.getStr(parameterName);
            if (value.startsWith("[")) {
                String list = parameterName.split("\\.")[0];
                value = StringUtils.isEmpty(othersMap.getStr(list)) ? "" : othersMap.getStr(list);
                JSONArray objects = JSONUtil.parseArray(value);
                List<Map> listMap = JSONUtil.toList(objects, Map.class);
                paramMap.put(list, listMap);
                continue;
            }
            paramMap.put(parameterName, value);
        }
        return paramMap;
    }

    protected abstract MessageType messageType();

    protected abstract boolean filter(User s);

    // 消息合法校验
    private boolean isValid(MessageBody messageBody) {
        return true;
    }

    // 查找场景+模板~内部定义为path,可以理解为路由
    private List<MessagePath> findSceneAndTemplate(MessageBody messageBody) {
        return messagePathService.findPathBySceneAndType(messageBody);
    }

    //替换变量
    protected void handleParam(Map<String, Object> paramMap, MessagePath path) {
        ExpressionHandler expressionHandler = expressionHandlerFactory.getExpressionHandler();
        path.setTitle(expressionHandler.execScript(path.getTitle(), paramMap));
        path.setTemplate(expressionHandler.execScript(path.getTemplate(), paramMap));
    }
}
