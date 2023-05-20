package com.center.message.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Getter
@Setter
public class MessageEvent extends ApplicationEvent {
    private MessageBody messageBody;

    public MessageEvent(Object source, MessageBody messageBody) {
        super(source);
        this.messageBody = messageBody;
    }
}
