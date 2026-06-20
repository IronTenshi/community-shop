package com.Fanggaozhiai.service.iml;

import com.Fanggaozhiai.context.Context;
import com.Fanggaozhiai.dto.OrderParam;
import com.Fanggaozhiai.entity.Order;
import com.Fanggaozhiai.entity.OrderItem;
import com.Fanggaozhiai.mapper.OrderItemMapper;
import com.Fanggaozhiai.mapper.OrderMapper;
import com.Fanggaozhiai.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class OrderServiceIml  implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public void add(OrderParam orderParam) {
        // 计算订单总金额
        Integer money = 0;
        for (OrderItem orderItem : orderParam.getOrderItems()) {
            money += orderItem.getTotal();
        }
        // 设置金额后插入订单，获取自增id
        orderParam.setMoney(money);
        orderMapper.add(orderParam);
        // 将订单id回填到每个明细项
        for (OrderItem orderItem : orderParam.getOrderItems()) {
            orderItem.setOrdId(orderParam.getId());
        }
        // 批量插入订单明细
        orderItemMapper.add(orderParam.getOrderItems());
    }

    @Override
    public List<Order> list(Integer id) {
        return orderMapper.list(id);
    }

    @Override
    public Order findById(Integer id) {
        //判断查询是否为本人查询
        Order order = orderMapper.selectById(id);
        if(order == null){
            log.info("订单不存在");
            throw new RuntimeException("订单不存在");
        }
        if(!Objects.equals(order.getUsId(), Context.getId())){
            log.info("无权限");
            throw new RuntimeException("无权限");
        }
        return order;
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        // 查询订单是否存在
        Order order = orderMapper.selectById(id);
        if (order == null) {
            log.info("订单不存在");
            throw new RuntimeException("订单不存在");
        }
        // 校验是否为本人操作
        if (!Objects.equals(order.getUsId(), Context.getId())) {
            log.info("无权限");
            throw new RuntimeException("无权限");
        }
        // 校验订单状态：已完成或已支付的订单不能取消
        if (order.getStage() == 1) {
            log.info("订单已完成");
            throw new RuntimeException("订单已完成");
        }
        if (order.getPayTime() != null) {
            log.info("订单已支付");
            throw new RuntimeException("订单已支付");
        }
        // 先删子表明细，再删主表订单
        orderItemMapper.delete(id);
        orderMapper.delete(id);
    }
}
