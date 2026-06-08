package com.Fanggaozhiai.controller.employee;

import com.Fanggaozhiai.dto.EmployeeLoginDTO;
import com.Fanggaozhiai.entity.Employee;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.EmployeeService;
import com.Fanggaozhiai.vo.LoginReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    //根据员工username 和 password 登录
    @PostMapping("/login")
    public Result login(@RequestBody EmployeeLoginDTO employeeLoginDTO){
        log.info("员工登录: {}", employeeLoginDTO);
        if(employeeLoginDTO == null){
            log.info("用户名或密码为空");
        }
        LoginReturn loginReturn = employeeService.login(employeeLoginDTO);
        if(loginReturn == null){
            return Result.error("登录失败");
        }
        log.info("登录成功: {}", loginReturn);
        return Result.success(loginReturn);
    }
    //注册 创建
    //根据 name username password 注册
    @PostMapping("/register")
    public Result register(@RequestBody Employee employee){
        log.info("注册信息: {}",employee);
        employeeService.register(employee);
        return Result.success();
    }
}
