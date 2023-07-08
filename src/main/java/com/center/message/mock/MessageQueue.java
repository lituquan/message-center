package com.center.message.mock;

import cn.hutool.json.JSONUtil;
import com.center.message.mock.chain.EmailUserHandler;
import com.center.message.mock.chain.SmsUserHandler;
import com.center.message.mock.chain.WechatUserHandler;
import com.center.message.mock.queue.ADisruptorConsumer;
import com.center.message.mock.queue.DisruptorQueue;
import com.center.message.mock.queue.DisruptorQueueFactory;
import com.center.message.model.MessageBody;
import com.center.message.model.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Slf4j
@Component
public class MessageQueue extends ADisruptorConsumer<String> implements Producer {
    private DisruptorQueue disruptorQueue;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    AbstractUserHandlerChain.Builder builder = new AbstractUserHandlerChain.Builder();

    @PostConstruct
    public void init() {
        builder.addHandler(new SmsUserHandler())
                .addHandler(new EmailUserHandler())
                .addHandler(new WechatUserHandler())
                .build();
        // 创建一个Disruptor队列操作类对象（RingBuffer大小为4，false表示只有一个生产者）
        disruptorQueue = DisruptorQueueFactory.getHandleEventsQueue(4,
                false, this);
    }

    // 模拟客户端调用MQ发送
    @Override
    public Void sendMessage(MessageBody messageBody) {
        disruptorQueue.add(JSONUtil.toJsonStr(messageBody));
        return null;
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
        if (isEmpty(messageBody.getSceneId())) {
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

    @Override
    public void consume(String body) {
        publishMessage(body);
    }
}
