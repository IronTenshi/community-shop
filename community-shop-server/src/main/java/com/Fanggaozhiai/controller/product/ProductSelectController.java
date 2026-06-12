package com.Fanggaozhiai.controller.product;

import com.Fanggaozhiai.dto.ProductPageParam;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productss")
public class ProductSelectController {

    @Autowired
    private ProductService productService;
    //查询所有商品信息
    //分页查询
    @GetMapping
    public Result list(@RequestBody ProductPageParam productPageParam){
        return Result.success(productService.list(productPageParam));
    }
}
