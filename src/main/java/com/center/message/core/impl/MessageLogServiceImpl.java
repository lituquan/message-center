package com.center.message.core.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.center.message.core.MessageLogService;
import com.center.message.enums.SendStatusType;
import com.center.message.model.entity.MessageLog;
import com.center.message.persistent.LogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageLogServiceImpl implements MessageLogService {
    private final LogMapper logMap;

    @Override
    public void addLog(MessageLog messageLog) {
        Assert.isTrue(messageLog != null && messageLog.getMessageId().length() > 0,
                "参数不合法");
        log.info("addLog:{}", JSONUtil.toJsonStr(messageLog));
        logMap.insert(messageLog);
    }

    @Override
    @Transactional
    public void updateLog(String messageId, SendStatusType statusType) {
        updateLog(messageId, statusType, null);
    }

    @Override
    @Transactional
    public void updateLog(String messageId, SendStatusType statusType, String message) {
        log.info("updateLog:statusType {}, messageId {}", messageId, statusType);
        LambdaQueryWrapper<MessageLog> wrapper = new QueryWrapper<MessageLog>().lambda();
        wrapper.eq(MessageLog::getMessageId, messageId);
        MessageLog messageLog = logMap.selectOne(wrapper);
        messageLog.setStatus(statusType);
        messageLog.setResult(message);
        messageLog.setUpdateTime(new Date());
        logMap.update(messageLog, wrapper);
    }
}
