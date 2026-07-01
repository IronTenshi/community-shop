package com.Fanggaozhiai.mapper;

import com.Fanggaozhiai.dto.user.UserLogin;
import com.Fanggaozhiai.dto.user.UserPageParam;
import com.Fanggaozhiai.dto.user.UserPut;
import com.Fanggaozhiai.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户数据访问层接口
 * 负责用户相关的数据库操作
 * 对应的XML映射文件位于：resources/com/Fanggaozhiai/mapper/UserMapper.xml
 */
@Mapper
public interface UserMapper {

    /**
     * 用户登录查询
     * 根据用户名和密码查询用户，用于身份验证
     *
     * @param userLogin 登录参数，包含 username 和 password
     * @return 用户实体，不存在则返回null
     */
    @Select("SELECT * FROM user WHERE username = #{username} AND password = #{password}")
    User findByUsernameAndPassword(UserLogin userLogin);

    /**
     * 用户注册
     *
     * @param user 用户信息，包含 username、phone、password、address、createTime
     */
    @Insert("INSERT INTO user(username, phone, password, address, create_time) VALUES(#{username},#{phone} , #{password},#{address},#{createTime})")
    void addByNameAndUsernameAndPassword(User user);

    /**
     * 分页查询用户列表
     * 支持按用户名模糊查询，与PageHelper分页插件配合使用
     *
     * @param userPageParam 查询参数，包含 page、pageSize、name
     * @return 用户列表
     */
    List<User> list(UserPageParam userPageParam);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户实体
     */
    @Select("SELECT * FROM user WHERE id = #{id}")
    User getInfo(Integer id);

    /**
     * 修改用户信息
     * 支持修改 username、password、phone、address
     *
     * @param user 修改参数，包含 id、username、password、phone、address
     */
    void update(UserPut user);

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     */
    @Delete("DELETE FROM user WHERE id = #{id}")
    void delete(Integer id);
}