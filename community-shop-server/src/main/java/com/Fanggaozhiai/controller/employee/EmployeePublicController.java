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

/**
 * 员工公开接口控制器
 * 提供无需登录即可访问的员工相关接口
 * 基础路径: /emp
 */
@Slf4j
@RestController
@RequestMapping("/emp")
public class EmployeePublicController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录接口
     * 根据用户名和密码进行身份验证，验证通过后返回JWT token
     *
     * @param employeeLogin 登录参数，包含 username(用户名) 和 password(密码)
     * @return 登录成功返回 LoginReturn 对象（包含 id、username、token），失败返回错误信息
     */
    @PostMapping("/login")
    public Result login(@RequestBody EmployeeLogin employeeLogin){
        log.info("员工登录: {}", employeeLogin);
        if(employeeLogin == null){
            log.info("用户名或密码为空");
        }
        LoginReturn loginReturn = employeeService.login(employeeLogin);
        if(loginReturn == null || loginReturn.getToken() == null){
            return Result.error("登录失败");
        }
        log.info("登录成功: {}", loginReturn);
        return Result.success(loginReturn);
    }

    /**
     * 分页查询员工列表接口（公开）
     * 无需登录即可查询员工信息，支持按姓名模糊查询
     *
     * @param employeePageParam 分页查询参数，包含 page(页码，默认1)、pageSize(每页数量，默认10)、name(姓名，可选)
     * @return 分页结果，包含总记录数和当前页员工列表
     */
    //分页查询员工（公开接口）
    @GetMapping
    public Result page(@RequestBody EmployeePageParam employeePageParam){
        log.info("分页查询员工: {}", employeePageParam);
        PageResult<Employee> pageResult = employeeService.list(employeePageParam);
        return Result.success(pageResult);
    }
}
