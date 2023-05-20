package com.center.message.controller;

import com.center.message.mock.MessageQueue;
import com.center.message.model.MessageBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@RestController
public class HealthCheck {
    @Autowired
    MessageQueue messageQueue;

    /**
     * {"users":[{"phone":"111","wechatId":"aaa","email":"1111@qq.mail"}],"scene":"1","messageType":"EMAIL","phone":"111","param":{"name":"Tony"}}
     *
     * @param messageBody
     * @return
     */
    @PostMapping("/test")
    public Object sendMessage(@RequestBody MessageBody messageBody) {
        messageQueue.sendMessage(messageBody);
        return "OK";
    }
}
