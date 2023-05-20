package com.center.message.core;

import com.center.message.model.MessageBody;
import com.center.message.model.MessagePath;

import java.util.List;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
public interface MessagePathService {

    List<MessagePath> findPathBySceneAndType(MessageBody messageBody);

}
