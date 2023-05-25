package com.center.message.model;

import com.center.message.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessagePath {
    private String title;
    private String template;
    private String scene;
    private MessageType messageType;
    private boolean isCorn; // 是否定时消息
    private String cron;  // 定时规则
    private String messageId;
    private Map<String, Object> param;
}
