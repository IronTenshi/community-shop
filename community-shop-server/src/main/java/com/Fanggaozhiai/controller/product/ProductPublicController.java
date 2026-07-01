package com.Fanggaozhiai.controller.product;

import com.Fanggaozhiai.dto.product.ProductPageParam;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品查询控制器
 * 提供商品分页查询接口（公开访问）
 * 基础路径: /productss
 */
@Slf4j
@RestController
@RequestMapping("/productss")
public class ProductPublicController {

    @Autowired
    private ProductService productService;

    /**
     * 分页查询商品列表接口
     * 支持按商品名称模糊查询，返回分页数据
     *
     * @param productPageParam 分页查询参数，包含 page(页码，默认1)、pageSize(每页数量，默认10)、name(商品名称，可选)
     * @return 分页结果，包含总记录数和当前页商品列表
     */
    //查询所有商品信息
    //分页查询
    @PostMapping
    public Result list(@RequestBody ProductPageParam productPageParam){
        return Result.success(productService.list(productPageParam));
    }
    //查询商铺中的商品
    @PostMapping("/merchant/{merId}")
    public Result selectByMerId(@PathVariable("merId") Integer merId){
        log.info("查询商铺商品列表，商铺ID: {}", merId);
        return Result.success(productService.selectByMerId(merId));
    }
}
