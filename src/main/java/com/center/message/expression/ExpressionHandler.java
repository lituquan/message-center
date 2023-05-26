package com.center.message.expression;

import java.util.Map;

public interface ExpressionHandler {
    /**
     * 模板渲染
     *
     * @param template
     * @param param
     * @return
     */
    String execScript(String template, Map<String, Object> param);
}
