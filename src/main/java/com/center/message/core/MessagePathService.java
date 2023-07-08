package com.center.message.core;

import com.center.message.model.MessageBody;
import com.center.message.model.MessagePathDTO;
import com.center.message.model.entity.MessagePath;
import com.center.message.model.entity.MessageTemplate;

import java.util.List;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
public interface MessagePathService {

    void addScene(String remark);

    void addTemplate(MessageTemplate template);

    void addPath(MessagePath path);

    List<MessagePathDTO> findPathBySceneAndType(MessageBody messageBody);

}
