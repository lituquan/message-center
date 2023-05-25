package com.center.message.core;

import com.center.message.model.MessageBody;
import lombok.extern.slf4j.Slf4j;

/**
 * Author: lituquan
 * Date: 2023/5/25
 */
@Slf4j
public abstract class AbstractMessageHandlerChain {
    private AbstractMessageHandlerChain nextHandler;

    /**
     * 返回false、异常会中断发送消息
     *
     * @param messageBody
     * @return
     */
    public abstract boolean handleMessage(MessageBody messageBody);

    public void setNextHandler(AbstractMessageHandlerChain nextHandler) {
        this.nextHandler = nextHandler;
    }

    public boolean handleAndNext(MessageBody messageBody) {
        boolean result = handleMessage(messageBody);
        if (result && nextHandler != null) {
            return nextHandler.handleAndNext(messageBody);
        }
        return result;
    }

    // 构建一个链表
    public static class Builder<T> {
        private AbstractMessageHandlerChain head;
        private AbstractMessageHandlerChain tail;

        public Builder<T> addAbstractMessageHandlerChain(AbstractMessageHandlerChain handler) {
            if (this.head == null) {
                this.head = this.tail = handler;
                return this;
            }
            this.tail.setNextHandler(handler);
            this.tail = handler;
            return this;
        }

        public AbstractMessageHandlerChain build() {
            return this.head;
        }
    }
}
