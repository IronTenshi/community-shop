package com.Fanggaozhiai.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商铺分页查询参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopPageParam {

    /** 页码，默认1 */
    private Integer page = 1;

    /** 每页数量，默认10 */
    private Integer pageSize = 10;

    /** 商铺名称（模糊查询） */
    private String name;
}