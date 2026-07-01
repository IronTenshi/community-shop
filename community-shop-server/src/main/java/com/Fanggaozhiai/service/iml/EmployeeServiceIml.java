package com.Fanggaozhiai.service.iml;

import com.Fanggaozhiai.dto.employee.EmployeeLogin;
import com.Fanggaozhiai.dto.employee.EmployeePageParam;
import com.Fanggaozhiai.dto.employee.EmployeePut;
import com.Fanggaozhiai.entity.Employee;
import com.Fanggaozhiai.mapper.EmployeeMapper;
import com.Fanggaozhiai.result.PageResult;
import com.Fanggaozhiai.service.EmployeeService;
import com.Fanggaozhiai.utils.JwtUtil;
import com.Fanggaozhiai.vo.LoginReturn;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工服务实现类
 * 负责员工相关的业务逻辑，包括登录注册、分页查询、信息修改
 */
@Service
public class EmployeeServiceIml implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     * 根据用户名和密码查询员工，验证通过后生成JWT token
     *
     * @param employeeLogin 登录参数，包含 username 和 password
     * @return LoginReturn 包含 id、username、token，失败时各字段为null
     */
    @Override
    public LoginReturn login(EmployeeLogin employeeLogin) {
        Employee employee = employeeMapper.findByUsernameAndPassword(employeeLogin);
        if(employee != null)
        {
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",employee.getId());
            claims.put("username",employee.getUsername());
            String token = JwtUtil.getTokenEmp(claims);
            return new LoginReturn(employee.getId(),employee.getUsername(),token,employee.getJob());
        }
        else {
            return new LoginReturn(null,null,null);
        }
    }

    /**
     * 员工注册
     * 自动设置入职时间为当前日期
     *
     * @param employee 注册信息，包含 name、username、password、job、stage
     */
    @Override
    public void register(Employee employee) {
        employee.setEntryTime(LocalDate.now());
        employeeMapper.addByNameAndUsernameAndPassword(employee);
    }

    /**
     * 分页模糊查询员工列表
     * 支持按姓名模糊匹配，按入职时间降序排列
     *
     * @param employeePageParam 分页查询参数，包含 page、pageSize、name
     * @return 分页结果
     */
    @Override
    public PageResult<Employee> list(EmployeePageParam employeePageParam) {
        PageHelper.startPage(employeePageParam.getPage(),employeePageParam.getPageSize());
        List<Employee> list = employeeMapper.list(employeePageParam);
        Page<Employee> page = (Page<Employee>) list;
        return new PageResult<>(page.getTotal(),page.getResult());
    }

    /**
     * 根据ID查询员工详情
     *
     * @param id 员工ID
     * @return 员工信息，不存在时返回null
     */
    @Override
    public Employee getInfo(Integer id) {
         return employeeMapper.selectById(id);
    }

    /**
     * 修改员工信息
     * 动态更新，只更新传入的非空字段
     *
     * @param employeePut 修改参数，包含 id 和要更新的字段
     */
    @Override
    public void update(EmployeePut employeePut) {
        employeeMapper.update(employeePut);
    }

    /**
     * 删除员工
     *
     * @param id 员工ID
     */
    @Override
    public void delete(Integer id) {
        employeeMapper.deleteById(id);
    }
}