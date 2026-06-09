package com.Fanggaozhiai.service.iml;

import com.Fanggaozhiai.dto.EmployeeLoginDTO;
import com.Fanggaozhiai.dto.EmployeePageParam;
import com.Fanggaozhiai.dto.EmployeePut;
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

@Service
public class EmployeeServiceIml implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    //登录
    @Override
    public LoginReturn login(EmployeeLoginDTO employeeLoginDTO) {
        // 根据用户名查询员工 返回员工
        Employee employee = employeeMapper.findByUsernameAndPassword(employeeLoginDTO);
        //查询到员工后 生成token
        if(employee != null)
        {
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",employee.getId());
            claims.put("username",employee.getUsername());
            String token = JwtUtil.getToken(claims);
            return new LoginReturn(employee.getId(),employee.getUsername(),token);
        }
        else {
            return new LoginReturn(null,null,null);
        }
    }

    //注册
    @Override
    public void register(Employee employee) {
        //设置entry time
        employee.setEntryTime(LocalDate.now());
        employeeMapper.addByNameAndUsernameAndPassword(employee);
    }

    @Override
    public PageResult<Employee> list(EmployeePageParam employeePageParam) {
        //开启分页
        PageHelper.startPage(employeePageParam.getPage(),employeePageParam.getPageSize());
        //查询到所有数据
        List<Employee> list = employeeMapper.list(employeePageParam);
        //强转为 list 类型以用来返回到前端
        Page<Employee> page = (Page<Employee>) list;
        //返回分页数据
        return new PageResult<>(page.getTotal(),page.getResult());
    }

    //通过 id 查询员工
    @Override
    public Employee getInfo(Integer id) {
         return employeeMapper.selectById(id);
    }

    //修改员工信息
    @Override
    public void update(EmployeePut employeePut) {
        employeeMapper.update(employeePut);
    }

    //删除员工
    @Override
    public void delete(Integer id) {
        employeeMapper.deleteById(id);
    }
}
