package com.Fanggaozhiai.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户修改参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPut {

    /** 用户ID */
    private Integer id;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 手机号 */
    private String phone;

    /** 收货地址 */
    private String address;
}