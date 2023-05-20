package com.center.message.core.listener;

import com.center.message.core.AbstractMessageHandler;
import com.center.message.enums.MessageType;
import com.center.message.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Slf4j
@Service
public class WechatMessageHandler extends AbstractMessageHandler {


    @Override
    protected MessageType messageType() {
        return MessageType.WECHAT;
    }

    @Override
    protected boolean filter(User s) {
        return !StringUtils.isEmpty(s.getWechatId());
    }
}
