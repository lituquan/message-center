package com.center.message.mock;

import cn.hutool.extra.spring.SpringUtil;
import com.center.message.model.MessageBody;

/**
 * Author: lituquan
 * Date: 2023/5/26
 */
public abstract class AbstractUserHandlerChain {
    protected AbstractUserHandlerChain chain;

    protected MockUserService getUserService() {
        return SpringUtil.getBean(MockUserService.class);
    }

    public void next(AbstractUserHandlerChain handler) {
        this.chain = handler;
    }

    public abstract void doHandle(MessageBody messageBody);

    public static class Builder {
        private AbstractUserHandlerChain head;
        private AbstractUserHandlerChain tail;

        public AbstractUserHandlerChain.Builder addHandler(AbstractUserHandlerChain handler) {
            if (this.head == null) {
                this.head = this.tail = handler;
                return this;
            }
            this.tail.next(handler);
            this.tail = handler;

            return this;
        }

        public AbstractUserHandlerChain build() {
            return this.head;
        }
    }


}


