package com.Fanggaozhiai.mapper;

import com.Fanggaozhiai.dto.UserLogin;
import com.Fanggaozhiai.dto.UserPageParam;
import com.Fanggaozhiai.dto.UserPut;
import com.Fanggaozhiai.entity.Employee;
import com.Fanggaozhiai.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    //根据用户名查询用户
    @Select("SELECT * FROM user WHERE username = #{username} AND password = #{password}")
    User findByUsernameAndPassword(UserLogin userLogin);

    //用户注册
    @Insert("INSERT INTO user(username, phone, password, address, create_time) VALUES(#{username},#{phone} , #{password},#{address},#{createTime})")
    void addByNameAndUsernameAndPassword(User user);

    //分页查询通过 username
    List<Employee> list(UserPageParam userPageParam);

    //查询单条用户信息
    @Select("SELECT * FROM user WHERE id = #{id}")
    User getInfo(Integer id);

    //修改用户信息
    void update(UserPut user);

    //删除用户
    @Delete("DELETE FROM user WHERE id = #{id}")
    void delete(Integer id);
}
