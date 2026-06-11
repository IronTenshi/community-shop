package com.Fanggaozhiai.controller.employee;

import com.Fanggaozhiai.dto.EmployeeLogin;
import com.Fanggaozhiai.dto.EmployeePageParam;
import com.Fanggaozhiai.entity.Employee;
import com.Fanggaozhiai.result.PageResult;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.EmployeeService;
import com.Fanggaozhiai.vo.LoginReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmployeeLoginAndRegisterAndSelectController {
    @Autowired
    private EmployeeService employeeService;
    //根据员工username 和 password 登录
    @PostMapping("/login")
    public Result login(@RequestBody EmployeeLogin employeeLogin){
        log.info("员工登录: {}", employeeLogin);
        if(employeeLogin == null){
            log.info("用户名或密码为空");
        }
        LoginReturn loginReturn = employeeService.login(employeeLogin);
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
    @GetMapping
    public Result page(@RequestBody EmployeePageParam employeePageParam){
        log.info("分页查询: {}", employeePageParam);
        //返回分页数据
        PageResult<Employee> pageResult = employeeService.list(employeePageParam);
        return Result.success(pageResult);
    }
    //查询返回单条详细数据
    //根据id查询
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable("id") Integer id){
        log.info("查询员工信息: {}", id);
        Employee employee = employeeService.getInfo(id);
        return Result.success(employee);
    }
}
