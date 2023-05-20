package com.center.message.mock;

import com.center.message.model.MessageBody;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Scanner;

@SpringBootTest
class MessageQueueTest {
    @Autowired
    MessageQueue messageQueue;

    @Test
    void sendSmsMessage() {
        MessageBody body = new MessageBody();
        body.setScene("1");
        body.setPhone("111");
        body.setParam(new HashMap() {{
            put("name", "Tony");
        }});
        messageQueue.sendMessage(body);
        Scanner scanner=new Scanner(System.in);
        scanner.next(); // 这个是避免主线程退出
    }

    @Test
    void sendEmailMessage() {
        MessageBody body = new MessageBody();
        body.setScene("2");
        body.setEmail("1111@qq.mail");
        body.setParam(new HashMap() {{
            put("items", "[{\"name\":\"你好\"}]");
        }});
        messageQueue.sendMessage(body);
        Scanner scanner=new Scanner(System.in);
        scanner.nextLine(); // 这个是避免主线程退出
    }
}