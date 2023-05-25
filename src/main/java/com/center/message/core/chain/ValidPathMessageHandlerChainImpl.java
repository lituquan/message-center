package com.center.message.core.chain;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.center.message.core.AbstractMessageHandlerChain;
import com.center.message.core.MessagePathService;
import com.center.message.model.MessageBody;
import com.center.message.model.MessagePath;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Author: lituquan
 * Date: 2023/5/25
 */
@Slf4j
public class ValidPathMessageHandlerChainImpl extends AbstractMessageHandlerChain {

    @Override
    public boolean handleMessage(MessageBody messageBody) {
        //2.获取已开启、可用的场景配置、模板信息
        List<MessagePath> finalPathDtoList = findSceneAndTemplate(messageBody);
        if (CollectionUtil.isEmpty(finalPathDtoList)) {
            log.info("onApplicationEvent, but this scene is not exist or opened.");
            return false;
        }
        messageBody.setFinalPathDtoList(finalPathDtoList);
        return true;
    }

    // 查找场景+模板~内部定义为path,可以理解为路由
    private List<MessagePath> findSceneAndTemplate(MessageBody messageBody) {
        MessagePathService messagePathService = SpringUtil.getBean(MessagePathService.class);
        return messagePathService.findPathBySceneAndType(messageBody);
    }

}
