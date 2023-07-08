package com.center.message.core;

import com.center.message.enums.SendStatusType;
import com.center.message.model.entity.MessageLog;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class MessageLogServiceTest {
    @SpyBean
    MessageLogService messageLogService;

    @Test
    public void testAddLog() {
        // 创建一个模拟的 MessageLog 对象
        MessageLog messageLog = new MessageLog();

        // 调用 addLog 方法
        messageLog.setMessageId("messageId");
        messageLogService.addLog(messageLog);

        // 验证 addLog 方法是否被调用一次
        verify(messageLogService, times(1)).addLog(messageLog);
    }

    @Test
    public void testUpdateLogWithSuccess() {

        // 调用 updateLog 方法
        messageLogService.updateLog("messageId", SendStatusType.SUCCESS);

        // 验证 updateLog 方法是否被调用一次，且参数正确
        verify(messageLogService, times(1)).updateLog("messageId", SendStatusType.SUCCESS);
    }

    @Test
    public void testUpdateLogWithException() {

        // 调用 updateLog 方法
        messageLogService.updateLog("messageId", SendStatusType.EXCEPTION, "error message");

        // 验证 updateLog 方法是否被调用一次，且参数正确
        verify(messageLogService, times(1)).updateLog("messageId", SendStatusType.EXCEPTION, "error message");
    }
}
