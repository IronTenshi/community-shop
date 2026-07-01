package com.Fanggaozhiai.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 员工登录参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLogin {

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;
}