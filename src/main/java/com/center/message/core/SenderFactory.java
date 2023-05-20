package com.center.message.core;

import cn.hutool.extra.spring.SpringUtil;
import com.center.message.enums.MessageType;
import com.center.message.mock.sender.EmailSender;
import com.center.message.mock.sender.Sender;
import com.center.message.mock.sender.SmsSender;
import com.center.message.mock.sender.WechatSender;
import org.springframework.util.Assert;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
public class SenderFactory {
    public static Sender findSender(MessageType messageType) {
        switch (messageType) {
            case SMS:
                return SpringUtil.getBean(SmsSender.class);
            case EMAIL:
                return SpringUtil.getBean(EmailSender.class);
            case WECHAT:
                return SpringUtil.getBean(WechatSender.class);
            default:
                Assert.isTrue(true, "Unknown MessageType");
        }
        return null;
    }
}
