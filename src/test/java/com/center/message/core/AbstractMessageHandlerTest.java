package com.center.message.core;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.center.message.core.listener.SmsMessageHandler;
import com.center.message.model.MessagePath;
import org.junit.Assert;
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
        Assert.assertTrue(true);
    }

    @Test
    void getParam() {
        String json = new FileReader("json/param.json").readString();
        JSONObject map = JSONUtil.parseObj(json);
        Map<String, Object> param = mockMessageHandler.getParam(map);
        Assert.assertNotNull(param);
        System.out.println(JSONUtil.toJsonStr(param));
    }
}