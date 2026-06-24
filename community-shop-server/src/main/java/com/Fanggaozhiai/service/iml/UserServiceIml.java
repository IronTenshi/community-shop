package com.Fanggaozhiai.service.iml;

import com.Fanggaozhiai.dto.user.UserLogin;
import com.Fanggaozhiai.dto.user.UserPageParam;
import com.Fanggaozhiai.dto.user.UserPut;
import com.Fanggaozhiai.entity.User;
import com.Fanggaozhiai.mapper.UserMapper;
import com.Fanggaozhiai.result.PageResult;
import com.Fanggaozhiai.service.UserService;
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
 * 用户服务实现类
 * 负责用户相关的业务逻辑，包括登录注册、分页查询、信息修改
 */
@Service
public class UserServiceIml implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * 根据用户名和密码查询用户，验证通过后生成JWT token
     *
     * @param userLogin 登录参数，包含 username 和 password
     * @return LoginReturn 包含 id、username、token，失败时各字段为null
     */
    @Override
    public LoginReturn login(UserLogin userLogin) {
        User user= userMapper.findByUsernameAndPassword(userLogin);
        if(user != null)
        {
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",user.getId());
            claims.put("username",user.getUsername());
            String token = JwtUtil.getTokenUser(claims);
            return new LoginReturn(user.getId(),user.getUsername(),token);
        }
        else {
            return new LoginReturn(null,null,null);
        }
    }

    /**
     * 用户注册
     * 自动设置注册时间为当前日期
     *
     * @param user 注册信息，包含 username、password、phone、address
     */
    @Override
    public void register(User user) {
        user.setCreateTime(LocalDate.now());
        userMapper.addByNameAndUsernameAndPassword(user);
    }

    /**
     * 分页模糊查询用户列表
     * 支持按用户名模糊匹配，按注册时间降序排列
     *
     * @param userPageParam 分页查询参数，包含 page、pageSize、username
     * @return 分页结果
     */
    @Override
    public PageResult<User> list(UserPageParam userPageParam) {
        PageHelper.startPage(userPageParam.getPage(),userPageParam.getPageSize());
        List<User> list = userMapper.list(userPageParam);
        Page<User> page = (Page<User>) list;
        return new PageResult<>(page.getTotal(),page.getResult());
    }

    /**
     * 根据ID查询用户详情
     *
     * @param id 用户ID
     * @return 用户信息，不存在时返回null
     */
    @Override
    public User getInfo(Integer id) {
        return userMapper.getInfo(id);
    }

    /**
     * 修改用户信息
     * 动态更新，只更新传入的非空字段
     *
     * @param user 修改参数，包含 id 和要更新的字段
     */
    @Override
    public void update(UserPut user) {
        userMapper.update(user);
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    @Override
    public void delete(Integer id) {
        userMapper.delete(id);
    }
}