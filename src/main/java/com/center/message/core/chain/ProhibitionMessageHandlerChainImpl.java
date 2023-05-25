package com.center.message.core.chain;

import cn.hutool.extra.spring.SpringUtil;
import com.center.message.core.AbstractMessageHandlerChain;
import com.center.message.core.ProhibitionService;
import com.center.message.model.MessageBody;
import lombok.extern.slf4j.Slf4j;

/**
 * 确认是否黑名单
 * Author: lituquan
 * Date: 2023/5/25
 */
@Slf4j
public class ProhibitionMessageHandlerChainImpl extends AbstractMessageHandlerChain {

    @Override
    public boolean handleMessage(MessageBody messageBody) {
        ProhibitionService prohibitionService = SpringUtil.getBean(ProhibitionService.class);
        return !prohibitionService.checkProhibition(messageBody);
    }
}
