package com.Fanggaozhiai.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 员工分页查询参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeePageParam {

    /** 页码，默认1 */
    private Integer page = 1;

    /** 每页数量，默认10 */
    private Integer pageSize = 10;

    /** 员工姓名（模糊查询） */
    private String name;
}