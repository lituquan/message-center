package com.center.message.model;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageJob {
    private Map<String, Object> param;
    private MessagePath path;
    private MessageBody messageBody;
    private String time;
}
