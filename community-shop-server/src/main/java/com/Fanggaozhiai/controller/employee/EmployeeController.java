package com.Fanggaozhiai.controller.employee;

import com.Fanggaozhiai.dto.EmployeeLoginDTO;
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
    //查询返回分页
    //输入页码,和名称,返回分页数据
    //页数默认1 页大小默认10 输入名称模糊查询
    @GetMapping
    public Result page(@RequestParam EmployeePageParam employeePageParam){
        log.info("分页查询: {}", employeePageParam);
        //返回分页数据
        PageResult<Employee> pageResult = employeeService.list(employeePageParam);
        return Result.success(pageResult);
    }
    //查询返回单条详细数据
    //根据id查询
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id){
        log.info("查询员工信息: {}", id);
        Employee employee = employeeService.getInfo(id);
        return Result.success(employee);
    }
    //修改员工信息
    /**
     * @param employee
     * @return Result
     */
    @PutMapping
    public Result update(@RequestParam Employee employee){
        log.info("修改员工信息: {}", employee);
        employeeService.update(employee);
        return Result.success();
    }
    //删除员工信息
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        log.info("删除员工信息: {}", id);
        employeeService.delete(id);
        return Result.success();
    }
}
