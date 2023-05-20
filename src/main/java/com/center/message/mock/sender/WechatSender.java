package com.center.message.mock.sender;

import com.center.message.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Slf4j
@Service
public class WechatSender implements Sender {
    @Override
    public void send(User user, String content) {
        log.warn("WechatId:{},Content:{}", user.getWechatId(), content);
    }
}
