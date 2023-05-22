package com.center.message.expression.impl;

import com.center.message.expression.ExpressionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Map;

/**
 * Author: lituquan
 * Date: 2023/5/21
 */
@Slf4j
@Component
public class VelocityExpressionHandler implements ExpressionHandler {
    private static final VelocityEngine VELOCITY_ENGINE = new VelocityEngine();

    static {
        VELOCITY_ENGINE.init();
    }

    @Override
    public String execScript(String script, Map<String, Object> param) {
        try {
            StringWriter writer = new StringWriter();
            VELOCITY_ENGINE.evaluate(new VelocityContext(param), writer, "", script);
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Velocity process fail!");
        }
        return script;
    }
}
