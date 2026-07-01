package com.Fanggaozhiai.controller.user;

import com.Fanggaozhiai.dto.user.UserLogin;
import com.Fanggaozhiai.dto.user.UserPageParam;
import com.Fanggaozhiai.entity.User;
import com.Fanggaozhiai.result.PageResult;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.UserService;
import com.Fanggaozhiai.vo.LoginReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户公开接口控制器
 * 提供无需登录即可访问的用户相关接口
 * 基础路径: /user
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserPublicController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录接口
     * 根据用户名和密码进行身份验证，验证通过后返回JWT token
     *
     * @param userLogin 登录参数，包含 username(用户名) 和 password(密码)
     * @return 登录成功返回 LoginReturn 对象（包含 id、username、token），失败返回错误信息
     */
    //登录功能
    //输入username password
    //return userLogin 需要判断是否为空，因为返回永远有值
    @PostMapping("/login")
    public Result login(@RequestBody UserLogin userLogin){
        log.info("用户登录: {}", userLogin);
        if(userLogin == null){
            log.info("用户名或密码为空");
            return Result.error("用户名或密码为空");
        }
        LoginReturn loginReturn = userService.login(userLogin);
        if(loginReturn == null || loginReturn.getToken() == null){
            return Result.error("登录失败");
        }
        log.info("登录成功: {}", loginReturn);
        return Result.success(loginReturn);
    }

    /**
     * 用户注册接口
     * 根据用户名、密码、手机号、收货地址创建新用户账号
     *
     * @param user 注册信息，包含 username(用户名)、password(密码)、phone(手机号)、address(收货地址)
     * @return 操作结果，成功返回 success
     */
    //注册 创建
    //根据 username password address phone注册
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        log.info("注册信息: {}",user);
        userService.register(user);
        return Result.success();
    }

    /**
     * 分页查询用户列表接口
     * 支持按用户名模糊查询，返回分页数据
     *
     * @param userPageParam 分页查询参数，包含 page(页码，默认1)、pageSize(每页数量，默认10)、username(用户名，可选)
     * @return 分页结果，包含总记录数和当前页用户列表
     */
    //查询返回分页
    //输入页码,和名称,返回分页数据
    //页数默认1 页大小默认10 输入 username 模糊查询
    @PostMapping
    public Result page(@RequestBody UserPageParam userPageParam){
        log.info("分页查询: {}", userPageParam);
        //返回分页数据
        PageResult<User> pageResult = userService.list(userPageParam);
        return Result.success(pageResult);
    }

    /**
     * 查询用户详情接口
     * 根据用户ID查询单个用户的详细信息
     *
     * @param id 用户ID，从URL路径中获取
     * @return 用户详细信息
     */
    //查询返回单条详细数据 by id
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable("id") Integer id){
        log.info("查询用户信息: {}", id);
        User user = userService.getInfo(id);
        return Result.success(user);
    }
}
