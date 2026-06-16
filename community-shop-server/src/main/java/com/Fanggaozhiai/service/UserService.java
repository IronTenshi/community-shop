package com.Fanggaozhiai.service;

import com.Fanggaozhiai.dto.UserLogin;
import com.Fanggaozhiai.dto.UserPageParam;
import com.Fanggaozhiai.dto.UserPut;
import com.Fanggaozhiai.entity.Employee;
import com.Fanggaozhiai.entity.User;
import com.Fanggaozhiai.result.PageResult;
import com.Fanggaozhiai.vo.LoginReturn;

public interface UserService {
    //根据 username password 登录查询
    LoginReturn login(UserLogin userLogin);

    // 注册
    void register(User user);

    //分页查询操作
    PageResult<User> list(UserPageParam userPageParam);

    //根据 id 查询
    User getInfo(Integer id);

    //修改用户信息
    void update(UserPut user);

    //删除用户信息
    void delete(Integer id);
}
