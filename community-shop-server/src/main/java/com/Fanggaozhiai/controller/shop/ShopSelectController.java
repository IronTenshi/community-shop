package com.Fanggaozhiai.controller.shop;

import com.Fanggaozhiai.dto.ShopPageParam;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/shopss")
public class ShopSelectController {
    @Autowired
    private ShopService shopService;
    //查询 所有商铺信息
    //模糊查询商铺信 name
    //分页
    @GetMapping
    public Result selectAll(@RequestBody ShopPageParam shopPageParam){
        log.info("查询商铺信息");
        return Result.success(shopService.list(shopPageParam));
    }
}
