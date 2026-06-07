package com.Fanggaozhiai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品实体类
 * 对应数据库表: product
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    /** 商品ID，主键，自动递增 */
    private Integer id;

    /** 商品名称 */
    private String name;

    /** 商品描述 */
    private String describe;

    /** 商品图片URL，存储于OSS */
    private String img;

    /** 状态: 0有货 1无货 */
    private Integer stage;

    /** 对应商家ID */
    private Integer merId;
}
