package com.Fanggaozhiai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 员工实体类
 * 对应数据库表: employee
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    /** 员工ID，主键，自动递增 */
    private Integer id;

    /** 姓名 */
    private String name;

    /** 状态: 0空闲 1配送中 */
    private Integer stage;

    /** 入职日期 */
    private LocalDate entryTime;

    /** 登录账号 */
    private String username;

    /** 登录密码 */
    private String password;

    /** 岗位: 0管理员 1配送员 */
    private Integer job;
}
