package com.Fanggaozhiai.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商铺修改参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopPut {

    /** 商铺ID */
    private Integer id;

    /** 商铺名称 */
    private String name;

    /** 联系人 */
    private String person;

    /** 联系电话 */
    private String phone;
}