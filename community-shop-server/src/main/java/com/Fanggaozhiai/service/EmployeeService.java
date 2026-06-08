package com.Fanggaozhiai.service;

import com.Fanggaozhiai.dto.EmployeeLoginDTO;
import com.Fanggaozhiai.entity.Employee;
import com.Fanggaozhiai.vo.LoginReturn;

public interface EmployeeService {
    //登录并且校验 token
    LoginReturn login(EmployeeLoginDTO employeeLoginDTO);

    // 注册
    void register(Employee employee);
}
