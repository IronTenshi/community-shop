package com.Fanggaozhiai.mapper;

import com.Fanggaozhiai.dto.EmployeeLogin;
import com.Fanggaozhiai.dto.EmployeePageParam;
import com.Fanggaozhiai.dto.EmployeePut;
import com.Fanggaozhiai.entity.Employee;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    //登录
    //根据用户名查询员工
    @Select("SELECT * FROM employee WHERE username = #{username} AND password = #{password}")
    Employee findByUsernameAndPassword(EmployeeLogin employeeLogin);

    //通过name entryTime username password添加员工
    @Insert("INSERT INTO employee(name, entry_time, username, password, job, stage) VALUES(#{name}, #{entryTime}, #{username}, #{password}, #{job}, #{stage})")
    void addByNameAndUsernameAndPassword(Employee employee);

    //通过 name 来模糊查询员工
    List<Employee> list(EmployeePageParam employeePageParam);

    //通过 id 查询员工
    @Select("SELECT * FROM employee WHERE id = #{id}")
    Employee selectById(Integer id);

    //修改员工信息
    //姓名、登录账号、登录密码、岗位
    void update(EmployeePut employeePut);

    @Delete("DELETE FROM employee WHERE id = #{id}")
    void deleteById(Integer id);
}
