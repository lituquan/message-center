package com.center.message.core;

import com.center.message.enums.SendStatusType;
import com.center.message.model.MessageLog;


public interface MessageLogService {
    void addLog(MessageLog messageLog);

    void updateLog(String messageId, SendStatusType success);

    void updateLog(String messageId, SendStatusType exception, String message);
}
