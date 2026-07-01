package com.Fanggaozhiai.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 员工修改参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeePut {

    /** 员工ID */
    private Integer id;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 姓名 */
    private String name;

    /** 岗位 */
    private Integer job;

    /** 状态 */
    private Integer stage;
}