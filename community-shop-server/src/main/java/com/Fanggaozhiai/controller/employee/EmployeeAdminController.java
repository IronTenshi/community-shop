package com.Fanggaozhiai.controller.employee;

import com.Fanggaozhiai.dto.employee.EmployeePut;
import com.Fanggaozhiai.entity.Employee;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 员工管理控制器（管理员专属）
 * 提供管理员对员工的增删改查操作
 * 基础路径: /admin/emps
 * 权限要求: 需要登录且 job=0（管理员）
 */
@Slf4j
@RestController
@RequestMapping("/admin/emps")
public class EmployeeAdminController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 新增员工接口
     * 管理员创建新员工账号
     *
     * @param employee 员工信息，包含 name(姓名)、username(登录账号)、password(密码)、job(岗位：0管理员 1配送员)、stage(状态：0空闲 1配送中)
     * @return 操作结果，成功返回 success
     */
    @PostMapping
    public Result register(@RequestBody Employee employee){
        log.info("管理员新增员工: {}",employee);
        employeeService.register(employee);
        return Result.success();
    }

    /**
     * 查询员工详情接口
     * 管理员根据员工ID查询员工详细信息
     *
     * @param id 员工ID，从URL路径中获取
     * @return 员工详细信息
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable("id") Integer id){
        log.info("管理员查询员工信息: {}", id);
        Employee employee = employeeService.getInfo(id);
        return Result.success(employee);
    }

    /**
     * 修改员工信息接口
     * 管理员修改指定员工的信息（姓名、账号、密码）
     *
     * @param employeePut 修改参数，包含 id(员工ID)、name(姓名)、username(账号)、password(密码)
     * @return 操作结果，成功返回 success
     */
    @PutMapping
    public Result update(@RequestBody EmployeePut employeePut){
        log.info("管理员修改员工信息: {}", employeePut);
        employeeService.update(employeePut);
        return Result.success();
    }

    /**
     * 删除员工接口
     * 管理员根据员工ID删除员工账号
     *
     * @param id 员工ID，从URL路径中获取
     * @return 操作结果，成功返回 success
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id){
        log.info("管理员删除员工信息: {}", id);
        employeeService.delete(id);
        return Result.success();
    }
}
