package com.Fanggaozhiai.controller.user;

import com.Fanggaozhiai.context.Context;
import com.Fanggaozhiai.dto.user.UserPut;
import com.Fanggaozhiai.entity.User;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器（用户专属）
 * 提供用户修改自身信息和删除账号的接口
 * 基础路径: /user
 * 权限要求: 需要用户token认证（经过 UserPermissionFilter）
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户信息接口
     * 从ThreadLocal上下文获取当前用户ID，查询并返回完整用户信息
     *
     * @return 当前登录用户的完整信息（包含 phone、address 等）
     */
    @GetMapping("/me")
    public Result getCurrentUserInfo(){
        Integer id = Context.getId();
        log.info("获取当前用户信息: id={}", id);
        User user = userService.getInfo(id);
        return Result.success(user);
    }

    /**
     * 修改用户信息接口
     * 用户修改自己的个人信息（用户名、密码、手机号、地址）
     *
     * @param userPut 修改参数，包含 id(用户ID)、username(用户名)、password(密码)、phone(手机号)、address(收货地址)
     * @return 操作结果，成功返回 success
     */
    //修改用户信息
    @PutMapping
    public Result update(@RequestBody UserPut userPut){
        //获取当前用户ID,防止越权管理
        Integer id = Context.getId();
        userPut.setId(id);
        log.info("修改用户信息: {}", userPut);
        userService.update(userPut);
        return Result.success();
    }

//    /**
//     * 删除用户接口
//     * 根据用户ID删除用户账号
//     *
//     * @param id 用户ID，从URL路径中获取
//     * @return 操作结果，成功返回 success
//     */
//    //删除用户信息
//    @DeleteMapping("/{id}")
//    public Result delete(@PathVariable("id") Integer id){
//        log.info("删除用户信息: {}", id);
//        userService.delete(id);
//        return Result.success();
//    }
}
