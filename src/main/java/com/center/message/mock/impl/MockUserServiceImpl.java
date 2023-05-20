package com.center.message.mock.impl;

import com.center.message.mock.MockUserService;
import com.center.message.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Slf4j
@Service
public class MockUserServiceImpl implements MockUserService {

    private ConcurrentMap<String, User> userMap = new ConcurrentHashMap() {{
        put("1", User.builder().email("1111@qq.mail").build());
        put("2", User.builder().email("2222@qq.mail").phone("111").wechatId("bbb").build());
        put("3", User.builder().phone("113").wechatId("ccc").build());
    }};

    @Override
    public User findUserByPhone(String phone) {
        log.info("phone:{}", phone);
        return userMap.values().stream().filter(user -> Objects.equals(phone, user.getPhone())).findFirst().get();
    }

    @Override
    public User findUserByEmail(String email) {
        log.info("email:{}", email);
        return userMap.values().stream().filter(user -> Objects.equals(email, user.getEmail())).findFirst().get();
    }

    @Override
    public User findUserByWechatId(String wechatId) {
        log.info("wechatId:{}", wechatId);
        return userMap.values().stream().filter(user -> Objects.equals(wechatId, user.getWechatId())).findFirst().get();
    }

}
