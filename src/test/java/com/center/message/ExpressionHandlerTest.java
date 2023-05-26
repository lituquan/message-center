package com.center.message;

import com.center.message.expression.ExpressionHandler;
import com.center.message.expression.impl.FreemarkerExpressionHandler;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * 这个是用GPT生成的单元测试
 */
public class ExpressionHandlerTest {

    private ExpressionHandler expressionHandler = new FreemarkerExpressionHandler(); // Initialize with your own implementation

    @Test
    public void testExecScriptWithValidTemplateAndParam() {
        String template = "Hello, ${name}!";
        Map<String, Object> param = new HashMap<>();
        param.put("name", "John");

        String result = expressionHandler.execScript(template, param);

        assertEquals("Result should be 'Hello, John!'", "Hello, John!", result);
    }

    @Test
    public void testExecScriptWithInvalidTemplate() {
        String template = "Hello, ${name!";
        Map<String, Object> param = new HashMap<>();
        param.put("name", "John");
        String result = expressionHandler.execScript(template, param);

        assertEquals(template, result);
    }

    @Test
    public void testExecScriptWithNullParam() {
        String template = "Hello, ${name}!";

        String result = expressionHandler.execScript(template, null);

        assertEquals(template, result);
    }
}
