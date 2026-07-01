package com.Fanggaozhiai.service;

import com.Fanggaozhiai.dto.order.OrderParam;
import com.Fanggaozhiai.entity.Order;

import java.util.List;

/**
 * 订单服务接口
 * 定义订单相关的业务操作，包括下单、查询、取消等
 */
public interface OrderService {

    /**
     * 下单
     * 用户提交订单，包含商品明细列表。后端进行校验：商品存在性、是否下架、同商铺校验、后端计价
     *
     * @param orderParam 订单参数，包含收货地址和订单明细列表
     */
    void add(OrderParam orderParam);

    /**
     * 查询当前用户的所有订单
     *
     * @param id 用户ID
     * @return 该用户的订单列表
     */
    List<Order> list(Integer id);

    /**
     * 查询订单详情
     * 只允许查询自己的订单（Service层做权限校验）
     *
     * @param id 订单ID
     * @return 订单详细信息
     */
    Order findById(Integer id);

    /**
     * 取消订单
     * 只能取消自己的订单，且订单状态必须为待配送（stage=0）
     * 删除时先删子表（order_item）再删主表（order）
     *
     * @param id 订单ID
     */
    void delete(Integer id);
}