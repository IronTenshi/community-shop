package com.Fanggaozhiai.service;

import com.Fanggaozhiai.dto.product.ProductPageParam;
import com.Fanggaozhiai.entity.Product;
import com.Fanggaozhiai.result.PageResult;

import java.util.List;

/**
 * 商品服务接口
 * 定义商品相关的业务操作，包括分页查询、添加、删除、上下架状态修改等
 */
public interface ProductService {

    /**
     * 分页查询商品列表
     * 支持按商品名称模糊查询
     *
     * @param productPageParam 分页查询参数，包含 page、pageSize、name
     * @return 分页结果
     */
    PageResult<Product> list(ProductPageParam productPageParam);

    /**
     * 添加商品
     * 只有商铺的持有者才能添加商品（Service层做权限校验）
     *
     * @param product 商品信息，包含 name、describe、img、price、merId
     */
    void add(Product product);

    /**
     * 删除商品
     * 只有商品所属商铺的持有者才能删除
     *
     * @param id 商品ID
     */
    void delete(Integer id);

    /**
     * 修改商品上下架状态
     * 商家可以随时设置商品为有货(0)或无货(1)，需要是商铺持有者
     *
     * @param id    商品ID
     * @param stage 0有货 1无货
     */
    void updateStage(Integer id, Integer stage);

    /**
     * 修改商品图片（删除图片）
     * 只有商品所属商铺的持有者才能修改
     *
     * @param id  商品ID
     * @param img 图片URL（传空字符串表示删除图片）
     */
    void updateImg(Integer id, String img);

    /**
     * 根据商铺ID查询商品列表
     *
     * @param merId 商铺ID
     * @return 该商铺下的所有商品列表
     */
    List<Product> selectByMerId(Integer merId);
}