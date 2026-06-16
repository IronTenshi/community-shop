package com.Fanggaozhiai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 订单实体类
 * 对应数据库表: order
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    /** 订单ID，主键，自动递增 */
    private Integer id;

    /** 对应用户ID，标识下单用户 */
    private Integer usId;

    /** 订单金额 */
    private Integer money;

    /** 状态: 0未送达 1已送达完成 */
    private Integer stage;

    /** 收货地址 */
    private String reAddress;

    /** 创建时间 */
    private LocalDate createTime;

    /** 支付时间，可空，空为未支付 */
    private LocalDate payTime;
}
