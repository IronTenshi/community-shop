package com.Fanggaozhiai.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商铺注册参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopRegister {

    /** 商铺名称 */
    private String name;

    /** 联系电话 */
    private String phone;

    /** 联系人 */
    private String person;
}