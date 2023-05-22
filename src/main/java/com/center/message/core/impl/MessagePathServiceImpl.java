package com.center.message.core.impl;

import cn.hutool.core.io.file.FileReader;
import com.center.message.core.MessagePathService;
import com.center.message.enums.MessageType;
import com.center.message.model.MessageBody;
import com.center.message.model.MessagePath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Slf4j
@Service
public class MessagePathServiceImpl implements MessagePathService {
    FileReader fileReaderFreeMarker = new FileReader("template/template.ftl");

    private ConcurrentMap<String, Map<MessageType, List<MessagePath>>> pathMap = new ConcurrentHashMap() {
        {
            put("1", new HashMap() {{
                put(MessageType.SMS,
                        Arrays.asList(MessagePath.builder().template("hello,${name}").build()));
            }});
            put("2", new HashMap() {{
                put(MessageType.EMAIL,
                        Arrays.asList(MessagePath.builder().title("主题").template(fileReaderFreeMarker.readString()).build()));
            }});
            put("3", new HashMap() {{
                put(MessageType.EMAIL,
                        Arrays.asList(MessagePath.builder().template(fileReaderFreeMarker.readString()).build()));
            }});

            put("4", new HashMap() {{
                put(MessageType.EMAIL,
                        Arrays.asList(MessagePath.builder().template(fileReaderFreeMarker.readString()).build()));
            }});
        }
    };

    @Override
    public List<MessagePath> findPathBySceneAndType(MessageBody messageBody) {
        return pathMap.get(messageBody.getScene()).get(messageBody.getMessageType());
    }
}
