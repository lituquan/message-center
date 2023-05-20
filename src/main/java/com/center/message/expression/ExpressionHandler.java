package com.center.message.expression;

import java.util.Map;

public interface ExpressionHandler {
    String execScript(String script, Map param);
}
