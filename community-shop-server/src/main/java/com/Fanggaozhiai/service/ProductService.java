package com.Fanggaozhiai.service;

import com.Fanggaozhiai.dto.product.ProductPageParam;
import com.Fanggaozhiai.entity.Product;
import com.Fanggaozhiai.result.PageResult;

public interface ProductService {
    //分页查询商品信息
    //name
    PageResult<Product> list(ProductPageParam productPageParam);

    //添加商品
    //name describe img
    void add(Product product);

    //删除商品
    void delete(Integer id);

    /**
     * 修改商品上下架状态
     * @param id    商品ID
     * @param stage 0有货 1无货
     */
    void updateStage(Integer id, Integer stage);
}
