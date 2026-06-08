package com.Fanggaozhiai.mapper;

import com.Fanggaozhiai.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {
    //根据用户名查询员工
    @Select("SELECT * FROM employee WHERE username = #{username} AND password = #{password}")
    Employee findByUsernameAndPassword();

    //通过name entryTime username password添加员工
    @Insert("INSERT INTO employee(name, entry_time, username, password) VALUES(#{name}, #{entryTime} ,#{username} ,#{password})")
    void addByNameAndUsernameAndPassword(Employee employee);
}
