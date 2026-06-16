package com.Fanggaozhiai.controller.employee;

import com.Fanggaozhiai.context.Context;
import com.Fanggaozhiai.dto.EmployeePut;
import com.Fanggaozhiai.entity.Employee;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 配送员接口控制器
 * 提供配送员查看和修改自身信息的接口
 * 基础路径: /delivery/emps
 * 权限要求: 需要登录且 job=1（配送员）
 */
@Slf4j
@RestController
@RequestMapping("/delivery/emps")
public class EmployeeDeliveryController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 查询当前配送员个人信息接口
     * 配送员查看自己的详细信息（从ThreadLocal获取当前登录用户ID）
     *
     * @return 当前登录配送员的详细信息
     */
    @GetMapping("/me")
    public Result getCurrentDeliveryInfo(){
        Integer id = Context.getId();
        log.info("配送员查询个人信息: {}", id);
        Employee employee = employeeService.getInfo(id);
        return Result.success(employee);
    }

    /**
     * 修改当前配送员信息接口
     * 配送员修改自己的个人信息（从ThreadLocal获取当前登录用户ID）
     *
     * @param employeePut 修改参数，包含 id(用户ID)、username(用户名)、password(密码)、phone(手机号)、address(收货地址)
     * @return 操作结果，成功返回 success
     */
    @PutMapping("/me")
    public Result updateCurrentDeliveryInfo(@RequestBody EmployeePut employeePut){
        Integer currentId = Context.getId();
        log.info("配送员修改个人信息: currentId={}, request={}", currentId, employeePut);
        // 强制使用当前登录用户ID，防止越权修改他人数据
        employeePut.setId(currentId);
        employeeService.update(employeePut);
        return Result.success();
    }
}
