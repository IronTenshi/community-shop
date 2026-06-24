package com.Fanggaozhiai.service;

import com.Fanggaozhiai.dto.employee.EmployeeLogin;
import com.Fanggaozhiai.dto.employee.EmployeePageParam;
import com.Fanggaozhiai.dto.employee.EmployeePut;
import com.Fanggaozhiai.entity.Employee;
import com.Fanggaozhiai.result.PageResult;
import com.Fanggaozhiai.vo.LoginReturn;

public interface EmployeeService {
    //登录并且校验 token
    LoginReturn login(EmployeeLogin employeeLogin);

    // 注册
    void register(Employee employee);

    //通过一个名称查询出所有员工
    PageResult<Employee> list(EmployeePageParam employeePageParam);

    //通过 id 查询员工
    Employee getInfo(Integer id);

    //通过 employee 的数据修改员工信息
    void update(EmployeePut employeeput);

    //通过 id 删除员工
    void delete(Integer id);
}
