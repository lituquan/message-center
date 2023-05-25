package com.center.message.model;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
    private String scene;
    // 参数
    private JSONObject param;
    // 消息类型
    private MessageType messageType;

    private List<MessagePath> finalPathDtoList;

    public void setParam(Map<String, Object> hashMap) {
        String json = JSONUtil.toJsonStr(hashMap);
        param = JSONUtil.parseObj(json);
    }
}
