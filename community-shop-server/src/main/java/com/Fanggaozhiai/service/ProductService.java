package com.Fanggaozhiai.service;

import com.Fanggaozhiai.dto.ProductPageParam;
import com.Fanggaozhiai.entity.Product;
import com.Fanggaozhiai.result.PageResult;

import java.util.List;

public interface ProductService {
    //分页查询商品信息
    //name
    PageResult<Product> list(ProductPageParam productPageParam);

    //添加商品
    //name describe img
    void add(Product product);

    //删除商品
    void delete(Integer id);
}
