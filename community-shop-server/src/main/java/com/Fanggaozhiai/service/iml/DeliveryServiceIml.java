package com.Fanggaozhiai.service.iml;

import com.Fanggaozhiai.context.Context;
import com.Fanggaozhiai.entity.Delivery;
import com.Fanggaozhiai.entity.Order;
import com.Fanggaozhiai.mapper.DeliveryMapper;
import com.Fanggaozhiai.mapper.OrderMapper;
import com.Fanggaozhiai.service.DeliveryService;
import com.Fanggaozhiai.vo.DeliveryOrderReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 配送服务实现类
 * 负责配送相关的业务逻辑，包括接单、完成配送、查询配送记录等
 */
@Service
public class DeliveryServiceIml implements DeliveryService {
    @Autowired
    private DeliveryMapper deliveryMapper;
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 获取空闲订单列表
     * 返回所有 stage = 0（未分配）的订单，供配送员选择接单
     *
     * @return 空闲订单列表
     */
    @Override
    public List<Order> list() {
        return orderMapper.listByStage();
    }

    /**
     * 接单
     * 从ThreadLocal获取当前登录员工ID，创建配送记录（stage = 1 配送中）
     *
     * @param ordId 订单ID
     * @param note  接单备注（可选）
     */
    @Override
    public void accept(Integer ordId, String note) {
        Integer usId = Context.getId();
        Delivery delivery = new Delivery();
        delivery.setEmpId(usId);
        delivery.setOrdId(ordId);
        delivery.setStage(1);
        delivery.setNote(note);
        deliveryMapper.addByDelivery(delivery);
    }

    /**
     * 完成配送
     * 使用事务确保两步操作原子性：
     * 1. 更新 delivery.stage = 2（已送达）并记录送达时间
     * 2. 更新 order.stage = 1（已完成）
     *
     * @param ordId 订单ID
     */
    @Override
    @Transactional
    public void complete(Integer ordId) {
        LocalDate d = LocalDate.now();
        deliveryMapper.complete(d, ordId);
        orderMapper.complete(ordId);
    }

    /**
     * 获取当前配送员的配送订单
     * 从ThreadLocal获取当前登录员工ID，查询其已接单的配送记录
     *
     * @return 配送订单信息
     */
    @Override
    public Order getMyOrder() {
        Integer id = Context.getId();
        return deliveryMapper.getByEmpId(id);
    }

    /**
     * 查询所有订单（管理员用）
     *
     * @return 所有订单列表
     */
    @Override
    public List<Order> selectAll() {
        return orderMapper.listAll();
    }

    /**
     * 根据订单ID查询订单详情及配送信息
     * 使用事务确保数据一致性
     *
     * @param id 订单ID
     * @return DeliveryOrderReturn 对象，包含 order 和 delivery 两个实体
     */
    @Override
    @Transactional
    public DeliveryOrderReturn selectById(Integer id) {
        Order order = orderMapper.selectById(id);
        Delivery delivery = deliveryMapper.selectByOrdId(id);
        return new DeliveryOrderReturn(delivery, order);
    }
}