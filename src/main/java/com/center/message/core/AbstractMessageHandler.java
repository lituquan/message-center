package com.center.message.core;

import cn.hutool.core.bean.BeanUtil;
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
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
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

    @Async
    @Override
    public void onApplicationEvent(MessageEvent event) {
        try {
            // 这里拷贝一个对象,避免渠道之间影响~因为并发修改了setUsers|setMessageType
            MessageBody messageBody = BeanUtil.copyProperties(event.getMessageBody(), MessageBody.class);
            handle(messageBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handle(MessageBody messageBody) {
        log.info("start to send {}, event: [{}]", messageType(), JSONUtil.toJsonStr(messageBody));
        List<User> userList = messageBody.getUsers().stream().filter(this::filter).collect(Collectors.toList());
        messageBody.setUsers(userList);
        messageBody.setMessageType(messageType());
        builder.build().handleAndNext(messageBody);
        if (CollectionUtils.isEmpty(messageBody.getFinalPathDtoList())) {
            return;
        }
        for (MessagePathDTO path : messageBody.getFinalPathDtoList()) {
            //2.1获取入参字段，拼装发送消息服务的入参
            Map<String, Object> paramMap = messageBody.getParam();
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

    protected abstract MessageType messageType();

    protected abstract boolean filter(User s);

    //替换变量
    protected void handleParam(Map<String, Object> paramMap, MessagePathDTO path) {
        ExpressionHandler expressionHandler = expressionHandlerFactory.getExpressionHandler();
        path.setTitle(expressionHandler.execScript(path.getTitle(), paramMap));
        path.setTemplate(expressionHandler.execScript(path.getTemplate(), paramMap));
    }
}






