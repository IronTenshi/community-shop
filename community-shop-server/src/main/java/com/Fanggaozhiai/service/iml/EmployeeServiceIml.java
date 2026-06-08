package com.Fanggaozhiai.service.iml;

import com.Fanggaozhiai.dto.EmployeeLoginDTO;
import com.Fanggaozhiai.entity.Employee;
import com.Fanggaozhiai.mapper.EmployeeMapper;
import com.Fanggaozhiai.service.EmployeeService;
import com.Fanggaozhiai.utils.JwtUtil;
import com.Fanggaozhiai.vo.LoginReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeServiceIml implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    //登录
    @Override
    public LoginReturn login(EmployeeLoginDTO employeeLoginDTO) {
        // 根据用户名查询员工 返回员工
        com.Fanggaozhiai.entity.Employee employee = employeeMapper.findByUsernameAndPassword();
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
}
