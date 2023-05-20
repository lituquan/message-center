package com.center.message.model;

import lombok.*;

/**
 * Author: lituquan
 * Date: 2023/5/20
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String userId;
    private String phone;
    private String email;
    private String wechatId;
}
