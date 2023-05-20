package com.center.message.expression.impl;

import cn.hutool.json.JSONUtil;
import com.center.message.expression.AbstractExpressionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;
import java.util.Map;

/**
 * Author: lituquan
 * Date: 2023/5/18
 */
@Slf4j
public class JavaScriptHandlerImpl extends AbstractExpressionHandler {
    private static final ScriptEngineManager JS_ENGINE_MANAGER;
    private static final String JS_ENGINE_NAME = "Nashorn";

    static {
        JS_ENGINE_MANAGER = new ScriptEngineManager();
    }

    @Override
    protected String symbol() {
        return "script";
    }

    @Override
    public String doExec(String js, Map<String, Object> paramObj) {
        ScriptEngine jsEngine = JS_ENGINE_MANAGER.getEngineByName(JS_ENGINE_NAME);
        ScriptContext scriptContext = new SimpleScriptContext();
        if (!CollectionUtils.isEmpty(paramObj.keySet())) {
            paramObj.forEach((key, value) -> {
                scriptContext.setAttribute(key, value, ScriptContext.ENGINE_SCOPE);
                log.info(key + ":" + JSONUtil.toJsonStr(value));
            });
        }
        try {
            jsEngine.setContext(scriptContext);
            return jsEngine.eval(js).toString();
        } catch (Exception e) {
            log.info("执行js失败，js：", e);
            return js;
        }
    }
}
