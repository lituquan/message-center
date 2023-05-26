package com.center.message.mock.chain;

import com.center.message.mock.AbstractUserHandlerChain;
import com.center.message.model.MessageBody;
import com.center.message.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * Author: lituquan
 * Date: 2023/5/26
 */
@Slf4j
public class SmsUserHandler extends AbstractUserHandlerChain {

    @Override
    public void doHandle(MessageBody messageBody) {
        if (!StringUtils.isEmpty(messageBody.getPhone())) {
            User user = getUserService().findUserByPhone(messageBody.getPhone());
            messageBody.getUsers().add(user);
        }
    }
}
