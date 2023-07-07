package com.center.message.mock;

import com.center.message.model.MessageBody;

public interface Producer {
    // 模拟客户端调用MQ发送
    void sendMessage(MessageBody messageBody);

}
