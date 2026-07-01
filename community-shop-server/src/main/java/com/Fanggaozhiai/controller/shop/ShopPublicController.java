package com.Fanggaozhiai.controller.shop;

import com.Fanggaozhiai.dto.shop.ShopPageParam;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商铺查询控制器
 * 提供商铺分页查询接口（公开访问）
 * 基础路径: /shopss
 */
@Slf4j
@RestController
@RequestMapping("/shopss")
public class ShopPublicController {
    @Autowired
    private ShopService shopService;

    /**
     * 分页查询所有商铺接口
     * 支持按商铺名称模糊查询，返回分页数据
     *
     * @param shopPageParam 分页查询参数，包含 page(页码，默认1)、pageSize(每页数量，默认10)、name(商铺名称，可选)
     * @return 分页结果，包含总记录数和当前页商铺列表
     */
    //查询 所有商铺信息
    //模糊查询商铺信 name
    //分页
    @PostMapping
    public Result selectAll(@RequestBody ShopPageParam shopPageParam){
        log.info("查询商铺信息");
        return Result.success(shopService.list(shopPageParam));
    }
}
