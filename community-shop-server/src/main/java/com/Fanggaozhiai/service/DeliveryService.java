package com.Fanggaozhiai.service;

import com.Fanggaozhiai.entity.Order;
import com.Fanggaozhiai.vo.DeliveryOrderReturn;

import java.util.List;

/**
 * 配送服务接口
 * 定义配送相关的业务操作，包括接单、完成配送、查询配送记录等
 */
public interface DeliveryService {

    /**
     * 获取空闲订单列表
     * 返回所有未被接单的订单（order.stage = 0），供配送员选择接单
     *
     * @return 空闲订单列表
     */
    List<Order> list();

    /**
     * 接单
     * 配送员选择订单后，创建配送记录（delivery.stage = 1 配送中）
     *
     * @param ordId 订单ID
     * @param note  接单备注（可选）
     */
    void accept(Integer ordId, String note);

    /**
     * 完成配送
     * 配送员确认送达后，更新配送状态为已送达（delivery.stage = 2），订单状态更新为已完成（order.stage = 1）
     *
     * @param ordId 订单ID
     */
    void complete(Integer ordId);

    /**
     * 获取当前配送员的配送订单列表
     * 从ThreadLocal获取当前登录员工ID，查询其所有已接单的配送记录及对应订单
     *
     * @return 配送订单信息列表
     */
    List<DeliveryOrderReturn> getMyOrder();

    /**
     * 查询所有订单（管理员用）
     *
     * @return 所有订单列表
     */
    List<Order> selectAll();

    /**
     * 根据订单ID查询订单详情及配送信息
     * 返回 DeliveryOrderReturn 对象，包含 order 和 delivery 两个实体
     *
     * @param id 订单ID
     * @return 订单详情及配送信息
     */
    DeliveryOrderReturn selectById(Integer id);
}