package com.center.message.core;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.center.message.core.chain.ProhibitionMessageHandlerChainImpl;
import com.center.message.core.chain.ValidMessageHandlerChainImpl;
import com.center.message.core.chain.ValidPathMessageHandlerChainImpl;
import com.center.message.core.chain.ValidUserMessageHandlerChainImpl;
import com.center.message.enums.MessageType;
import com.center.message.expression.ExpressionHandler;
import com.center.message.expression.ExpressionHandlerFactory;
import com.center.message.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractMessageHandler implements ApplicationListener<MessageEvent> {
    @Autowired
    private MessageJobService messageJobService;
    @Autowired
    SendClient sendClient;
    @Autowired
    ExpressionHandlerFactory expressionHandlerFactory;

    AbstractMessageHandlerChain.Builder builder = new AbstractMessageHandlerChain.Builder();

    @PostConstruct
    public void initChains() {
        builder.addAbstractMessageHandlerChain(new ValidMessageHandlerChainImpl())
                .addAbstractMessageHandlerChain(new ValidUserMessageHandlerChainImpl())
                .addAbstractMessageHandlerChain(new ProhibitionMessageHandlerChainImpl())
                .addAbstractMessageHandlerChain(new ValidPathMessageHandlerChainImpl());
    }

    @Async("normalThreadPool")
    @Override
    public void onApplicationEvent(MessageEvent event) {
        try {
            handle(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handle(MessageEvent event) {
        log.info("start to send {}, event: [{}]", messageType(), JSONUtil.toJsonStr(event));
        MessageBody messageBody = event.getMessageBody();
        List<User> userList = messageBody.getUsers().stream().filter(this::filter).collect(Collectors.toList());
        messageBody.setUsers(userList);
        messageBody.setMessageType(messageType());
        builder.build().handleAndNext(messageBody);
        for (MessagePath path : messageBody.getFinalPathDtoList()) {
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
            path.setMessageType(messageType());
            //2.5发送
            sendClient.sendMessage(path, userList);
        }
    }

    // 处理参数
    public Map<String, Object> getParam(JSONObject othersMap) {
        Map<String, Object> paramMap = new HashMap<>(8);
        for (String parameterName : othersMap.keySet()) {
            String value = StringUtils.isEmpty(othersMap.getStr(parameterName)) ? "" : othersMap.getStr(parameterName);
            if (value.startsWith("[")) {
                JSONArray objects = JSONUtil.parseArray(value);
                List<Map> listMap = JSONUtil.toList(objects, Map.class);
                paramMap.put(parameterName, listMap);
                continue;
            }
            paramMap.put(parameterName, othersMap.get(parameterName));
        }
        return paramMap;
    }

    protected abstract MessageType messageType();

    protected abstract boolean filter(User s);

    //替换变量
    protected void handleParam(Map<String, Object> paramMap, MessagePath path) {
        ExpressionHandler expressionHandler = expressionHandlerFactory.getExpressionHandler();
        path.setTitle(expressionHandler.execScript(path.getTitle(), paramMap));
        path.setTemplate(expressionHandler.execScript(path.getTemplate(), paramMap));
    }
}
