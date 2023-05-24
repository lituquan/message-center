package com.center.message.core.impl;

import com.center.message.core.MessageLogService;
import com.center.message.enums.SendStatusType;
import com.center.message.model.MessageLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Slf4j
@Service
public class MessageLogServiceImpl implements MessageLogService {
    private final ConcurrentMap<String, MessageLog> logMap = new ConcurrentHashMap();

    @Override
    public void addLog(MessageLog messageLog) {
        logMap.put(messageLog.getMessageId(), messageLog);
    }

    @Override
    public void updateLog(String messageId, SendStatusType statusType) {
        logMap.get(messageId).setStatus(statusType);
    }

    @Override
    public void updateLog(String messageId, SendStatusType statusType, String message) {
        MessageLog messageLog = logMap.get(messageId);
        messageLog.setStatus(statusType);
        messageLog.setResult(message);
    }
}
