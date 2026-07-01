package com.Fanggaozhiai.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录返回对象
 * 包含用户/员工登录成功后的返回信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginReturn {

    /** 用户/员工ID */
    private Integer id;

    /** 用户名 */
    private String username;

    /** JWT认证令牌 */
    private String token;
}