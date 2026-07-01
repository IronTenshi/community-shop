package com.Fanggaozhiai.service;

import com.Fanggaozhiai.dto.employee.EmployeeLogin;
import com.Fanggaozhiai.dto.employee.EmployeePageParam;
import com.Fanggaozhiai.dto.employee.EmployeePut;
import com.Fanggaozhiai.entity.Employee;
import com.Fanggaozhiai.result.PageResult;
import com.Fanggaozhiai.vo.LoginReturn;

/**
 * 员工服务接口
 * 定义员工相关的业务操作，包括登录、注册、查询、修改、删除等
 */
public interface EmployeeService {

    /**
     * 员工登录
     * 根据用户名和密码进行身份验证，验证通过后返回包含JWT token的登录信息
     *
     * @param employeeLogin 登录参数，包含 username 和 password
     * @return 登录成功返回 LoginReturn（含id、username、token），失败返回null
     */
    LoginReturn login(EmployeeLogin employeeLogin);

    /**
     * 注册员工
     * 管理员创建新员工账号
     *
     * @param employee 员工信息，包含 name、username、password、job、stage
     */
    void register(Employee employee);

    /**
     * 分页查询员工列表
     * 支持按姓名模糊查询
     *
     * @param employeePageParam 分页查询参数，包含 page、pageSize、name
     * @return 分页结果
     */
    PageResult<Employee> list(EmployeePageParam employeePageParam);

    /**
     * 根据ID查询员工信息
     *
     * @param id 员工ID
     * @return 员工详细信息
     */
    Employee getInfo(Integer id);

    /**
     * 修改员工信息
     * 支持修改姓名、用户名、密码
     *
     * @param employeeput 修改参数，包含 id、name、username、password
     */
    void update(EmployeePut employeeput);

    /**
     * 根据ID删除员工
     *
     * @param id 员工ID
     */
    void delete(Integer id);
}