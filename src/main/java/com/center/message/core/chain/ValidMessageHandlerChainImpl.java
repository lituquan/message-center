package com.center.message.core.chain;

import com.center.message.core.AbstractMessageHandlerChain;
import com.center.message.model.MessageBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 消息合法校验
 * Author: lituquan
 * Date: 2023/5/25
 */
@Slf4j
@Component
public class ValidMessageHandlerChainImpl extends AbstractMessageHandlerChain {

    //1.1.校验入参
    @Override
    public boolean handleMessage(MessageBody messageBody) {
        return isValid(messageBody);
    }

    // 消息合法校验
    private boolean isValid(MessageBody messageBody) {
        return true;
    }
}
