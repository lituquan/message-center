package com.center.message.expression.impl;

import cn.hutool.core.io.file.FileReader;
import com.center.message.expression.ExpressionHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
class DefaultHandlerImplTest {

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
    void execFreeMarker() {
        exec(new FreemarkerExpressionHandler(), "template/template.ftl");
    }

    @Test
    void execVelocity() {
        exec(new VelocityExpressionHandler(), "template/template.vm");
    }

    void exec(ExpressionHandler handler, String fileName) {
        FileReader fileReader = new FileReader(fileName);
        String script = fileReader.readString();
        String result = handler.execScript(script, param);
        Assert.assertTrue(!result.contains("item.name"));
        log.info(result);
    }
}