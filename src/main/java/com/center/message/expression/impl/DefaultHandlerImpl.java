package com.center.message.expression.impl;

import com.center.message.expression.ExpressionHandler;
import com.center.message.util.PlaceholderUtils;

import java.util.Map;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
public class DefaultHandlerImpl implements ExpressionHandler {

    @Override
    public String execScript(String script, Map param) {
        return PlaceholderUtils.resolvePlaceholders(script, param);
    }
}
