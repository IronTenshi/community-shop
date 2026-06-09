package com.Fanggaozhiai.controller.user;

import com.Fanggaozhiai.dto.UserLoginDTO;
import com.Fanggaozhiai.dto.UserPageParam;
import com.Fanggaozhiai.entity.Employee;
import com.Fanggaozhiai.entity.User;
import com.Fanggaozhiai.result.PageResult;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.UserService;
import com.Fanggaozhiai.vo.LoginReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserLoginAndRegisterAndSelectController {
    @Autowired
    private UserService userService;
    //登录功能
    //输入username password phone
    @PostMapping("/login")
    public Result login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("用户登录: {}", userLoginDTO);
        if(userLoginDTO == null){
            log.info("用户名或密码为空");
        }
        LoginReturn loginReturn = userService.login(userLoginDTO);
        if(loginReturn == null){
            return Result.error("登录失败");
        }
        log.info("登录成功: {}", loginReturn);
        return Result.success(loginReturn);
    }
    //注册 创建
    //根据 username password address phone注册
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        log.info("注册信息: {}",user);
        userService.register(user);
        return Result.success();
    }
    //查询返回分页
    //输入页码,和名称,返回分页数据
    //页数默认1 页大小默认10 输入 username 模糊查询
    @GetMapping
    public Result page(@RequestBody UserPageParam userPageParam){
        log.info("分页查询: {}", userPageParam);
        //返回分页数据
        PageResult<Employee> pageResult = userService.list(userPageParam);
        return Result.success(pageResult);
    }
    //查询返回单条详细数据 by id
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable("id") Integer id){
        log.info("查询用户信息: {}", id);
        Employee employee = userService.getInfo(id);
        return Result.success(employee);
    }
}
