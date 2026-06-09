package com.Fanggaozhiai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//修改员工信息
//name username password
public class EmployeePut {
    private Integer id;
    private String username;
    private String password;
    private String name;
}
