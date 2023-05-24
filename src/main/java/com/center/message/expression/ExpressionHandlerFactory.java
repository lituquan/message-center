package com.center.message.expression;

import cn.hutool.extra.spring.SpringUtil;
import com.center.message.expression.impl.FreemarkerExpressionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Author: lituquan
 * Date: 2023/5/21
 */
@Component
public class ExpressionHandlerFactory {
    // 默认使用freemarker
    @Value("${message.template.engine:com.center.message.expression.impl.FreemarkerExpressionHandler}")
    private String engine;

    public ExpressionHandler getExpressionHandler() {
        try {
            Class clz = Class.forName(engine);
            return (ExpressionHandler) SpringUtil.getBean(clz);
        } catch (Exception e) {
            e.printStackTrace();
            // 默认使用freemarker
            return SpringUtil.getBean(FreemarkerExpressionHandler.class);
        }
    }

}
