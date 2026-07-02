package com.Fanggaozhiai.mapper;

import com.Fanggaozhiai.dto.employee.EmployeeLogin;
import com.Fanggaozhiai.dto.employee.EmployeePageParam;
import com.Fanggaozhiai.dto.employee.EmployeePut;
import com.Fanggaozhiai.entity.Employee;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 员工数据访问层接口
 * 负责员工相关的数据库操作
 * 对应的XML映射文件位于：resources/com/Fanggaozhiai/mapper/EmployeeMapper.xml
 */
@Mapper
public interface EmployeeMapper {

    /**
     * 员工登录查询
     * 根据用户名和密码查询员工，用于身份验证
     *
     * @param employeeLogin 登录参数，包含 username 和 password
     * @return 员工实体，不存在则返回null
     */
    @Select("SELECT * FROM employee WHERE username = #{username} AND password = #{password}")
    Employee findByUsernameAndPassword(EmployeeLogin employeeLogin);

    /**
     * 新增员工
     * 管理员创建新员工账号
     *
     * @param employee 员工信息，包含 name、entryTime、username、password、job、stage
     */
    @Insert("INSERT INTO employee(name, entry_time, username, password, job, stage) VALUES(#{name}, #{entryTime}, #{username}, #{password}, #{job}, #{stage})")
    void addByNameAndUsernameAndPassword(Employee employee);

    /**
     * 分页查询员工列表
     * 支持按姓名模糊查询，与PageHelper分页插件配合使用
     *
     * @param employeePageParam 查询参数，包含 page、pageSize、name
     * @return 员工列表
     */
    List<Employee> list(EmployeePageParam employeePageParam);

    /**
     * 根据ID查询员工
     *
     * @param id 员工ID
     * @return 员工实体
     */
    @Select("SELECT * FROM employee WHERE id = #{id}")
    Employee selectById(Integer id);

    /**
     * 修改员工信息
     * 支持修改姓名、登录账号、登录密码、岗位
     *
     * @param employeePut 修改参数，包含 id、name、username、password
     */
    void update(EmployeePut employeePut);

    /**
     * 根据ID删除员工
     *
     * @param id 员工ID
     */
    @Delete("DELETE FROM employee WHERE id = #{id}")
    void deleteById(Integer id);


    /**
     * 将员工状态设置为配送中（接单时调用）
     *
     * @param usId 员工ID
     */
    @Update("UPDATE employee SET stage = 1 WHERE id = #{usId}")
    void getById(Integer usId);

    /**
     * 将员工状态设置为空闲（配送完成时调用）
     *
     * @param id 员工ID
     */
    @Update("UPDATE employee SET stage = 0 WHERE id = #{id}")
    void completeById(Integer id);
}