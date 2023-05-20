package com.center.message.core;

import com.center.message.core.listener.SmsMessageHandler;
import com.center.message.model.MessagePath;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class AbstractMessageHandlerTest {

    AbstractMessageHandler mockMessageHandler = new SmsMessageHandler();

    @Test
    void handleParam() {
        Map<String, Object> map = new HashMap() {{
            put("name", "Tony");
        }};
        MessagePath path = MessagePath.builder().template("hello,${name}").build();
        mockMessageHandler.handleParam(map, path);
    }
}