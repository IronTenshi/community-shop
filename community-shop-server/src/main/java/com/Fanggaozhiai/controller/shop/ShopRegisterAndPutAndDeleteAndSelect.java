package com.Fanggaozhiai.controller.shop;

import com.Fanggaozhiai.dto.ShopPut;
import com.Fanggaozhiai.dto.ShopRegister;
import com.Fanggaozhiai.entity.Merchant;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商铺注册、修改、删除与查询控制器
 * 提供商铺的注册、信息修改、删除、查询自己商铺等接口
 * 基础路径: /shops
 * 权限要求: 需要用户token认证（经过 UserPermissionFilter）
 */
@Slf4j
@RestController
@RequestMapping("/shops")
public class ShopRegisterAndPutAndDeleteAndSelect {
    @Autowired
    private ShopService shopService;

    /**
     * 注册商铺接口
     * 用户创建商铺，自动与当前登录用户ID绑定
     *
     * @param shopRegister 注册信息，包含 name(商铺名称)、person(联系人)、phone(联系电话)
     * @return 操作结果，成功返回 success
     */
    //注册与当前用户 id 绑定的商铺
    //传入 name person phone
    @PostMapping("/register")
    public Result register(@RequestBody ShopRegister shopRegister){
        log.info("注册");
        shopService.register(shopRegister);
        return Result.success();
    }

    /**
     * 修改商铺信息接口
     * 商铺持有者修改自己的商铺信息
     *
     * @param shopPut 修改参数，包含 id(商铺ID)、name(商铺名称)、person(联系人)、phone(联系电话)
     * @return 操作结果，成功返回 success
     */
    //修改商铺信息
    //传入id
    //name person phone
    @PostMapping("/update")
    public Result update(@RequestBody ShopPut shopPut){
        log.info("修改商铺信息: {}", shopPut);
        shopService.update(shopPut);
        return Result.success();
    }

    /**
     * 删除商铺接口
     * 根据商铺ID删除商铺
     *
     * @param id 商铺ID，从URL路径中获取
     * @return 操作结果，成功返回 success
     */
    //删除商铺信息
    @DeleteMapping ("/{id}")
    public Result delete(@PathVariable("id") Integer id){
        log.info("删除商铺信息: {}", id);
        shopService.delete(id);
        return Result.success();
    }

    /**
     * 查询当前用户商铺接口
     * 查询当前登录用户的所有商铺信息
     *
     * @return 当前用户的所有商铺列表
     */
    //查询商铺信息
    //查询自己的商铺
    @GetMapping
    public Result selectByUsId(){
        log.info("查询自身商铺信息");
        return Result.success(shopService.selectByUsId());
    }
//    //进入商铺
//    //输入商铺id
//    //返回商品列表
//    @GetMapping("/{id}")
//    public Result enterShop(@PathVariable("id") Integer id){
//        log.info("进入商铺: {}", id);
//        shopService.enterShop(id);
//        return Result.success();
//    }
}
