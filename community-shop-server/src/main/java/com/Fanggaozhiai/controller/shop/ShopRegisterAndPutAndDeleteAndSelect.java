package com.Fanggaozhiai.controller.shop;

import com.Fanggaozhiai.dto.ShopPut;
import com.Fanggaozhiai.dto.ShopRegister;
import com.Fanggaozhiai.entity.Merchant;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/shops")
public class ShopRegisterAndPutAndDeleteAndSelect {
    @Autowired
    private ShopService shopService;
    //注册与当前用户 id 绑定的商铺
    //传入 name person phone
    @PostMapping("/register")
    public Result register(@RequestBody ShopRegister shopRegister){
        log.info("注册");
        shopService.register(shopRegister);
        return Result.success();
    }
    //修改商铺信息
    //传入id
    //name person phone
    @PostMapping("/update")
    public Result update(@RequestBody ShopPut shopPut){
        log.info("修改商铺信息: {}", shopPut);
        shopService.update(shopPut);
        return Result.success();
    }
    //删除商铺信息
    @DeleteMapping ("/{id}")
    public Result delete(@PathVariable("id") Integer id){
        log.info("删除商铺信息: {}", id);
        shopService.delete(id);
        return Result.success();
    }
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
