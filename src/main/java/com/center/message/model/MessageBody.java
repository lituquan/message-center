package com.center.message.model;

import com.center.message.enums.MessageType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Data
public class MessageBody {
    private String phone;
    private String email;
    private String wechatId;
    // 接收者
    private List<User> users;
    // 场景
    private Integer sceneId;
    // 参数
    private Map<String, Object> param;
    // 消息类型
    private MessageType messageType;

    private List<MessagePathDTO> finalPathDtoList;
}
