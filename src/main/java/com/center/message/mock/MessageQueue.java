package com.center.message.mock;

import cn.hutool.json.JSONUtil;
import com.center.message.mock.chain.EmailUserHandler;
import com.center.message.mock.chain.SmsUserHandler;
import com.center.message.mock.chain.WechatUserHandler;
import com.center.message.model.MessageBody;
import com.center.message.model.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Slf4j
@Component
public class MessageQueue {
    private BlockingDeque<String> blockingDeque = new LinkedBlockingDeque<>();
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private MockUserService userService;

    AbstractUserHandlerChain.Builder builder = new AbstractUserHandlerChain.Builder();

    @PostConstruct
    public void init() {
        builder.addHandler(new SmsUserHandler())
                .addHandler(new EmailUserHandler())
                .addHandler(new WechatUserHandler())
                .build();
    }

    // 模拟客户端调用MQ发送
    public void sendMessage(MessageBody messageBody) {
        blockingDeque.add(JSONUtil.toJsonStr(messageBody));
    }

    // 模拟MQ接收消息+广播消息
    private void receiveMessage() {
        try {
            String body = blockingDeque.take();
            publishMessage(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 这里需要处理消息幂等
    private void publishMessage(String msg) {
        //1.判定是否是json字符串
        if (!JSONUtil.isJsonObj(msg)) {
            log.warn("json formation error:{}", msg);
            return;
        }
        //2.校验必填入参
        MessageBody messageBody = JSONUtil.toBean(msg, MessageBody.class);
        if (isEmpty(messageBody.getScene())) {
            log.warn("parameter messageCode is missing:{}", msg);
            return;
        }
        //2.1 三者全都为空，排除
        if (isEmpty(messageBody.getPhone())
                && isEmpty(messageBody.getEmail())
                && isEmpty(messageBody.getWechatId())
                && isEmpty(messageBody.getUsers())) {
            log.warn("publish, but userList or receiver is null. json: [{}]", msg);
            return;
        }
        fillSendingToUsers(messageBody);
        //2.2 分给对应渠道去处理发送逻辑
        eventPublisher.publishEvent(new MessageEvent(this, messageBody));
    }

    // 填写接收人~userId转成手机号、邮箱、企微号
    // 这个其实可以下放到消息处理那里,并且可以实现用策略模式控制
    private void fillSendingToUsers(MessageBody messageBody) {
        if (messageBody.getUsers() == null) {
            messageBody.setUsers(new LinkedList<>());
        }
        builder.build().doHandle(messageBody);
    }

    // 模拟MQ保持监听~这里不考虑服务可靠性
    // @PostConstruct 会在服务开始启动这个监听
    @PostConstruct
    public void startServer() {
        new Thread(() -> {
            while (true) {
                receiveMessage();
            }
        }).start();
    }
}
