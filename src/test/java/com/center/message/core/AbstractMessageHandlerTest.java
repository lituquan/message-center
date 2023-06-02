package com.center.message.core;

import com.center.message.core.listener.SmsMessageHandler;
import com.center.message.model.MessagePath;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class AbstractMessageHandlerTest {

    @Autowired
    SmsMessageHandler mockMessageHandler;

    @Test
    void handleParam() {
        Map<String, Object> map = new HashMap() {{
            put("name", "Tony");
        }};
        MessagePath path = MessagePath.builder().template("hello,${name}").build();
        mockMessageHandler.handleParam(map, path);
        Assert.assertTrue(path.getTemplate().equals("hello,Tony"));
    }
}