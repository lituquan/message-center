package com.center.message.expression.impl;

import com.center.message.expression.AbstractExpressionHandler;
import com.center.message.util.PlaceholderUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Author: lituquan
 * Date: 2023/5/18
 */
@Slf4j
public class ForeachHandlerImpl extends AbstractExpressionHandler {
    public static final Pattern PATTERN_PARAM = Pattern.compile("\\$\\{(.+?)\\}");

    @Override
    protected String symbol() {
        return "foreach";
    }

    /**
     * [${items.name}](https://www.baidu.com)
     *
     * @param js
     * @param paramObj
     * @return
     */
    @Override
    public String doExec(String js, Map paramObj) {
        // 设置参数
        Matcher matcher = PATTERN_PARAM.matcher(js);
        List<String> params = new LinkedList<>();
        while (matcher.find()) {
            params.add(matcher.group());
        }
        if (params.size() == 0) {
            return "";
        }

        List<String> fields = new LinkedList<>();
        String collect = params.get(0);
        matcher = PATTERN_PARAM.matcher(collect);
        if (matcher.find()) {
            log.info("collect:" + matcher.group(1).split("\\.")[0]);
            collect = matcher.group(1).split("\\.")[0];
            IntStream.range(0, params.size()).forEach(index -> {
                String field = params.get(index);
                Matcher matcher2 = PATTERN_PARAM.matcher(field);
                if (matcher2.find()) {
                    fields.add(matcher2.group(1).split("\\.")[1]);
                }
            });
        }
        List list = (List) paramObj.get(collect);
        StringBuilder builder = new StringBuilder();
        list.forEach(item -> {
            String temp = js.substring(0);
            Map<String, Object> itemMap = (Map<String, Object>) item;
            for (int index = 0; index < params.size(); index++) {
                String field = fields.get(index);
                Object obj = itemMap.get(field);
                String value = obj == null ? "" : obj.toString();
                temp = PlaceholderUtils.resolvePlaceholders(temp, params.get(index), value);
            }
            builder.append(temp);
        });
        log.info(builder.toString());
        return builder.toString();
    }

}
