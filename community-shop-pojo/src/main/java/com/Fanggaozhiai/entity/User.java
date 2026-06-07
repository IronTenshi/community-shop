package com.Fanggaozhiai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 用户实体类
 * 对应数据库表: user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /** 用户ID，主键，自动递增 */
    private Integer id;

    /** 用户名 */
    private String username;

    /** 手机号 */
    private String phone;

    /** 登录密码 */
    private String password;

    /** 收货地址 */
    private String address;

    /** 注册时间 */
    private LocalDate createTime;
}
