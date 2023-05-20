package com.center.message.expression.impl;

import com.center.message.expression.AbstractExpressionHandler;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Author: lituquan
 * Date: 2023/5/18
 */
@Slf4j
public class GroovyHandlerImpl extends AbstractExpressionHandler {
    @Override
    public String doExec(String groovy, Map<String, Object> paramObj) {
        GroovyShell groovyShell = new GroovyShell();
        paramObj.entrySet().forEach(e -> groovyShell.setVariable(e.getKey(), e.getValue()));
        try {
            Object result = groovyShell.evaluate(groovy);
            return result.toString();
        } catch (Exception e) {
            log.info("执行groovy失败，groovy：", e);
            return groovy;
        }
    }

    @Override
    protected String symbol() {
        return "groovy";
    }
}
