package com.Fanggaozhiai.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;
}