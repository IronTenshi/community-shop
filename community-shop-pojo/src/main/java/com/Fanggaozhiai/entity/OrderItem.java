package com.Fanggaozhiai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单明细实体类
 * 对应数据库表: order_item
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    /** 明细ID，主键，自动递增 */
    private Integer id;

    /** 对应订单ID */
    private Integer ordId;

    /** 对应商品ID */
    private Integer proId;

    /** 商品数量 */
    private Integer num;

    /** 商品单价 */
    private double perMoney;

    /** 商品总价 */
    private double total;
}
