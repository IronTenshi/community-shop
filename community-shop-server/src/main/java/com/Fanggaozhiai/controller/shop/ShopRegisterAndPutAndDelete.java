package com.Fanggaozhiai.controller.shop;

import com.Fanggaozhiai.dto.ShopPut;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/shops")
public class ShopRegisterAndPutAndDelete {
    @Autowired
    private ShopService shopService;
    //注册与当前用户 id 绑定的商铺
    @PostMapping("/register")
    public Result register(){
        log.info("注册");
        shopService.register();
        return Result.success();
    }
    //修改商铺信息
    //name person phone
    @PostMapping("/update")
    public Result update(@RequestBody ShopPut shopPut){
        log.info("修改商铺信息");
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
}
