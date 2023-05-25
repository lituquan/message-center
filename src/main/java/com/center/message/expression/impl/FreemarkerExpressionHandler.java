package com.center.message.expression.impl;

import com.center.message.expression.ExpressionHandler;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Map;

/**
 * Author: lituquan
 * Date: 2023/5/21
 */
@Slf4j
@Component
public class FreemarkerExpressionHandler implements ExpressionHandler {
    private final static String TEMPLATE_NAME = "template";

    @Override
    public String execScript(String template, Map<String, Object> paramObj) {
        if (template == null) {
            return "";
        }
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate(TEMPLATE_NAME, template);
            cfg.setTemplateLoader(stringTemplateLoader);
            cfg.setClassicCompatible(true);
            Template templateProcessor = cfg.getTemplate(TEMPLATE_NAME);
            StringWriter writer = new StringWriter();
            templateProcessor.process(paramObj, writer);
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("freemarker process fail!");
        }
        return template;
    }
}
