package com.Fanggaozhiai.service;

import com.Fanggaozhiai.dto.user.UserLogin;
import com.Fanggaozhiai.dto.user.UserPageParam;
import com.Fanggaozhiai.dto.user.UserPut;
import com.Fanggaozhiai.entity.User;
import com.Fanggaozhiai.result.PageResult;
import com.Fanggaozhiai.vo.LoginReturn;

/**
 * 用户服务接口
 * 定义用户相关的业务操作，包括登录、注册、查询、修改、删除等
 */
public interface UserService {

    /**
     * 用户登录
     * 根据用户名和密码进行身份验证，验证通过后返回包含JWT token的登录信息
     *
     * @param userLogin 登录参数，包含 username 和 password
     * @return 登录成功返回 LoginReturn（含id、username、token），失败返回null
     */
    LoginReturn login(UserLogin userLogin);

    /**
     * 用户注册
     * 创建新用户账号
     *
     * @param user 用户信息，包含 username、password、phone、address
     */
    void register(User user);

    /**
     * 分页查询用户列表
     * 支持按用户名模糊查询
     *
     * @param userPageParam 分页查询参数，包含 page、pageSize、name
     * @return 分页结果
     */
    PageResult<User> list(UserPageParam userPageParam);

    /**
     * 根据ID查询用户信息
     *
     * @param id 用户ID
     * @return 用户详细信息
     */
    User getInfo(Integer id);

    /**
     * 修改用户信息
     * 支持修改用户名、密码、手机号、收货地址
     *
     * @param user 修改参数，包含 id、username、password、phone、address
     */
    void update(UserPut user);

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     */
    void delete(Integer id);
}