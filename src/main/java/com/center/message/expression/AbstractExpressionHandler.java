package com.center.message.expression;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
public abstract class AbstractExpressionHandler implements ExpressionHandler {

    protected abstract String symbol();

    @Override
    public String execScript(String content, Map param) {
        Pattern pattern = Pattern.compile(String.format("<%s>(.*?)</%s>", symbol(), symbol()), Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        // 暂时约定，同一个标记只允许出现一次。只处理一次
        if (matcher.find()) {
            String scriptContent = matcher.group(1);
            return content.replace(matcher.group(), doExec(scriptContent, param));
        }
        return content;
    }

    public abstract String doExec(String js, Map<String, Object> paramObj);

}
