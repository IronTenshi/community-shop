package com.Fanggaozhiai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商家实体类
 * 对应数据库表: merchant
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Merchant {

    /** 商家ID，主键，自动递增 */
    private Integer id;

    /** 商家名称 */
    private String name;

    /** 联系人 */
    private String person;

    /** 联系电话 */
    private String phone;

    /** 对应用户ID */
    private Integer usId;
}
