package com.Fanggaozhiai.vo;

import com.Fanggaozhiai.entity.Delivery;
import com.Fanggaozhiai.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配送订单返回对象
 * 包含订单信息和配送信息，用于管理员查看订单详情
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryOrderReturn {

    /** 配送记录 */
    private Delivery delivery;

    /** 订单信息 */
    private Order order;
}