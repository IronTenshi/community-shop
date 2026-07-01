package com.Fanggaozhiai.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录返回对象
 * 包含用户/员工登录成功后的返回信息
 */
@Data
@NoArgsConstructor
public class LoginReturn {

    /** 用户/员工ID */
    private Integer id;

    /** 用户名 */
    private String username;

    /** JWT认证令牌 */
    private String token;

    /** 岗位（仅员工登录时返回）：0管理员 1配送员 */
    private Integer job;

    /** 用户/员工登录构造函数（不含job） */
    public LoginReturn(Integer id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }

    /** 员工登录构造函数（含job） */
    public LoginReturn(Integer id, String username, String token, Integer job) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.job = job;
    }
}