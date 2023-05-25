package com.center.message.core.chain;

import cn.hutool.core.collection.CollectionUtil;
import com.center.message.core.AbstractMessageHandlerChain;
import com.center.message.model.MessageBody;
import lombok.extern.slf4j.Slf4j;

/**
 * 接收者是否存在
 * Author: lituquan
 * Date: 2023/5/25
 */
@Slf4j
public class ValidUserMessageHandlerChainImpl extends AbstractMessageHandlerChain {

    @Override
    public boolean handleMessage(MessageBody messageBody) {
        //1.2.过滤
        return CollectionUtil.isNotEmpty(messageBody.getUsers());
    }
}
