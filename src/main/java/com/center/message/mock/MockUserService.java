package com.center.message.mock;

import com.center.message.model.User;

/**
 * 访问者模式
 * Author: lituquan
 * Date: 2023/5/20
 */
public interface MockUserService {
    User findUserByPhone(String phone);

    User findUserByEmail(String email);

    User findUserByWechatId(String wechatId);

}
