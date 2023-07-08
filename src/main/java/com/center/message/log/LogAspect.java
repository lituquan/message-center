package com.center.message.log;

import cn.hutool.json.JSONUtil;
import com.center.message.core.MessageLogService;
import com.center.message.enums.SendStatusType;
import com.center.message.model.MessagePathDTO;
import com.center.message.model.entity.MessageLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Date;
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

    @Pointcut("execution(* com.center.message.core.SendClient.sendMessage(..))")
    public void pointcut() {
    }

    // add log
    @Before("pointcut()")
    public void before(JoinPoint joinpoint) {
        MessagePathDTO path = getPath(joinpoint);
        if (path == null) {
            return;
        }
        MessageLog messageLog = genLog(path);
        messageLogService.addLog(messageLog);
    }

    private MessageLog genLog(MessagePathDTO path) {
        MessageLog messageLog = new MessageLog();
        Map<String, Object> paramMap = path.getParam();
        String messageId = path.getMessageId();
        messageLog.setStatus(SendStatusType.SENDING);
        messageLog.setParam(JSONUtil.toJsonStr(paramMap));
        messageLog.setMessageId(messageId);
        messageLog.setCreateTime(new Date());
        messageLog.setPathId(path.getId());
        return messageLog;
    }

    // update log
    @After("pointcut()")
    public void after(JoinPoint joinPoint) {
        MessagePathDTO path = getPath(joinPoint);
        if (path == null) {
            return;
        }
        messageLogService.updateLog(path.getMessageId(), SendStatusType.SUCCESS);
    }

    @AfterThrowing(value = "pointcut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        MessagePathDTO path = getPath(joinPoint);
        if (path == null) {
            return;
        }
        messageLogService.updateLog(path.getMessageId(), SendStatusType.EXCEPTION, e.getMessage());
    }

    private MessagePathDTO getPath(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof MessagePathDTO) {
                MessagePathDTO path = (MessagePathDTO) args[i];
                return path;
            }
        }
        return null;
    }

    // @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            before(joinPoint);
            Object result = joinPoint.proceed();
            after(joinPoint);
            return result;
        } catch (Exception e) {
            afterThrowing(joinPoint, e);
            throw e;
        }
    }
}
