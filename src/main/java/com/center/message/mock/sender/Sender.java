package com.center.message.mock.sender;

import com.center.message.model.User;

public interface Sender {
    void send(User user, String content);
}
