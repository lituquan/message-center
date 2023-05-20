package com.center.message.expression.impl;

import cn.hutool.core.io.file.FileReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
class DefaultHandlerImplTest {
    ForeachHandlerImpl defaultHandler = new ForeachHandlerImpl();
    JavaScriptHandlerImpl jsHandler = new JavaScriptHandlerImpl();
    GroovyHandlerImpl groovyHandler = new GroovyHandlerImpl();

    Map param = new HashMap() {{
        put("user", "LiXiaoXiao");
        put("items", Arrays.asList(
                new HashMap() {{
                    put("name", "Monday");
                }},
                new HashMap() {{
                    put("name", "Friday");
                }})
        );
    }};

    @Test
    void execScript() {
        log.info("纯 js");
        FileReader fileReader = new FileReader("template/template.js");
        String script = fileReader.readString();

        Assert.assertTrue(script.contains("item.name"));
        String result = jsHandler.execScript(script, param);
        Assert.assertTrue(!result.contains("item.name"));
        log.info(result);
    }

    @Test
    void replaceScript() {
        String template = "这里是编程的世界\n" +
                "<foreach>[${items.name}](http://www.baidu.com)\n</foreach>" +
                "时间会消逝";
        log.info(template);
        String result = defaultHandler.execScript(template, param);
        Assert.assertTrue(!result.contains("item.name"));
        log.info(result);
    }

    @Test
    void execGroovy() {
        FileReader fileReader = new FileReader("template/template.groovy");
        String script = fileReader.readString();

        String result = groovyHandler.execScript(script, param);
        Assert.assertTrue(!result.contains("item.name"));
        log.info(result);
    }
}