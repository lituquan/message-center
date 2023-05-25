package com.center.message.core;

import com.center.message.log.LogAspect;
import com.center.message.mock.sender.Sender;
import com.center.message.model.MessagePath;
import com.center.message.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用于分离发送者和消息上下文，提供一个入口记录log
 * <p>
 * Author: lituquan
 * Date: 2023/5/24
 *
 * @see LogAspect#pointcut()
 */
@Slf4j
@Component
public class SendClient {

    public void sendMessage(MessagePath path, List<User> userList) {
        Sender sender = SenderFactory.findSender(path.getMessageType());
        log.info("sender is:{}", sender.getClass().getName());
        userList.forEach(user -> sender.send(user, path.getTemplate()));
    }
}
