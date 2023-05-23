package com.center.message.log;

import cn.hutool.json.JSONUtil;
import com.center.message.core.MessageLogService;
import com.center.message.enums.SendStatusType;
import com.center.message.model.MessageLog;
import com.center.message.model.MessagePath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Author: lituquan
 * Date: 2023/5/24
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {
    private final MessageLogService messageLogService;

    // add log
    @Before("execution(* com.center.message.core.AbstractMessageHandler.sendMessage(..))")
    public void addLog(JoinPoint joinpoint) {
        MessagePath path = getPath(joinpoint);
        Map paramMap = path.getParam();
        String messageId = path.getMessageId();
        MessageLog messageLog = new MessageLog();
        messageLog.setStatus(SendStatusType.SENDING);
        messageLog.setParam(JSONUtil.toJsonStr(paramMap));
        messageLog.setMessageId(messageId);
        //2.4保存待发送日志~日志可以用Aop处理
        messageLogService.addLog(messageLog);
        return;
    }

    // update log
    @After("execution(* com.center.message.core.AbstractMessageHandler.sendMessage(..))")
    public void updateLog(JoinPoint joinPoint) {
        MessagePath path = getPath(joinPoint);
        messageLogService.updateLog(path.getMessageId(), SendStatusType.SUCCESS);
    }

    @AfterThrowing(value = "execution(* com.center.message.core.AbstractMessageHandler.sendMessage(..))", throwing = "e")
    public void updateLogThrow(JoinPoint joinPoint, Throwable e) {
        MessagePath path = getPath(joinPoint);
        messageLogService.updateLog(path.getMessageId(), SendStatusType.EXCEPTION, e.getMessage());
    }

    private MessagePath getPath(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof MessagePath) {
                MessagePath path = (MessagePath) args[i];
                return path;
            }
        }
        return null;
    }
}
