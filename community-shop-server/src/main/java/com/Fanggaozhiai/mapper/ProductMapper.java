package com.Fanggaozhiai.mapper;

import com.Fanggaozhiai.dto.ProductPageParam;
import com.Fanggaozhiai.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品数据访问层接口
 * 负责商品相关的数据库操作
 * 对应的XML映射文件位于：resources/com/Fanggaozhiai/mapper/ProductMapper.xml
 */
@Mapper
public interface ProductMapper {

    /**
     * 分页模糊查询商品列表
     * 支持按商品名称进行模糊匹配
     * 需要与PageHelper分页插件配合使用
     *
     * @param productPageParam 查询参数，包含商品名称（可选，用于模糊查询）
     * @return 商品列表（未分页的完整列表，PageHelper会自动处理分页）
     */
    List<Product> list(ProductPageParam productPageParam);

    /**
     * 添加商品
     * 将商品信息插入到product表中
     * id字段由数据库自增生成
     *
     * @param product 商品实体对象，包含名称、描述、图片、状态、所属商铺ID等信息
     */
    void add(Product product);

    /**
     * 根据ID查询商品
     * 用于权限校验时获取商品的mer_id
     *
     * @param id 商品ID
     * @return 商品实体，包含mer_id等信息
     */
    @Select("SELECT * FROM product WHERE id = #{id}")
    Product selectById(Integer id);

    /**
     * 根据ID删除商品
     * 需配合权限校验，确保只有商铺持有者才能删除
     *
     * @param id 商品ID
     */
    void deleteById(Integer id);
}
