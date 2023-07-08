package com.center.message.model;

import com.center.message.model.entity.MessageTemplate;
import lombok.Data;

import java.util.Map;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Data
public class MessagePathDTO extends MessageTemplate {
    private Integer id;
    private Integer sceneId;
    private boolean isCorn; // 是否定时消息
    private String cron;  // 定时规则
    private String messageId;
    private Map<String, Object> param;
}
