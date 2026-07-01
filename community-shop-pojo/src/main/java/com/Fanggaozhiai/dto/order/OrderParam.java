package com.Fanggaozhiai.dto.order;

import com.Fanggaozhiai.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 订单参数
 * 用户下单时传递的参数，包含收货地址和订单明细列表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderParam {

    /** 订单ID，自增回填 */
    private Integer id;

    /** 用户ID，从ThreadLocal获取，不信任前端传值 */
    private Integer usId;

    /** 订单金额，后端计算，不信任前端传值 */
    private Double money;

    /** 收货地址 */
    private String reAddress;

    /** 创建时间，后端自动设置 */
    private LocalDate createTime;

    /** 订单明细列表 */
    private List<OrderItem> orderItems;
}