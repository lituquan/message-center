package com.center.message.model;

import com.center.message.enums.SendStatusType;
import lombok.Data;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Data
public class MessageLog {
    private SendStatusType status;
    private String param;
    private String messageId;
    private String result;
}
