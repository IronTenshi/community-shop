package com.Fanggaozhiai.service.iml;

import com.Fanggaozhiai.context.Context;
import com.Fanggaozhiai.dto.order.OrderParam;
import com.Fanggaozhiai.entity.Order;
import com.Fanggaozhiai.entity.OrderItem;
import com.Fanggaozhiai.entity.Product;
import com.Fanggaozhiai.mapper.OrderItemMapper;
import com.Fanggaozhiai.mapper.OrderMapper;
import com.Fanggaozhiai.mapper.ProductMapper;
import com.Fanggaozhiai.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 订单服务实现类
 * 负责订单相关的业务逻辑，包括下单校验、权限校验、取消订单等
 */
@Slf4j
@Service
public class OrderServiceIml  implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * 添加订单
     * 校验流程：
     * 1. 订单明细不能为空
     * 2. 逐项校验商品存在性、未下架(stage=0有货)、同商铺
     * 3. 使用后端价格重新计算每项明细的perMoney和total
     * 4. 插入订单并回填自增ID到明细项
     * 5. 批量插入订单明细
     *
     * @param orderParam 订单参数，包含收货地址和订单明细列表
     */
    @Transactional
    @Override
    public void add(OrderParam orderParam) {
        List<OrderItem> items = orderParam.getOrderItems();
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("订单明细不能为空");
        }
        // 校验商品：存在性、未下架、同商铺，同时用后端价格计算金额
        Integer merId = null;
        double money = 0;
        for (OrderItem item : items) {
            Product product = productMapper.selectById(item.getProId());
            if (product == null) {
                throw new RuntimeException("商品不存在: proId=" + item.getProId());
            }
            // 0有货 1无货（下架）
            if (product.getStage() != 0) {
                throw new RuntimeException("商品已下架: " + product.getName());
            }
            // 同商铺校验
            if (merId == null) {
                merId = product.getMerId();
            } else if (!Objects.equals(merId, product.getMerId())) {
                throw new RuntimeException("订单中商品必须属于同一商铺");
            }
            // 使用后端价格，不信任前端传值
            item.setPerMoney(product.getPrice());
            item.setTotal(product.getPrice() * item.getNum());
            money += item.getTotal();
        }
        orderParam.setMoney(money);
        // 插入订单，获取自增id
        orderMapper.add(orderParam);
        // 将订单id回填到每个明细项
        for (OrderItem item : items) {
            item.setOrdId(orderParam.getId());
        }
        // 批量插入订单明细
        orderItemMapper.add(items);
    }

    /**
     * 查询用户订单列表
     *
     * @param id 用户ID
     * @return 订单列表
     */
    @Override
    public List<Order> list(Integer id) {
        return orderMapper.list(id);
    }

    /**
     * 查询订单详情
     * 校验是否为本人查询，非本人抛出异常
     *
     * @param id 订单ID
     * @return 订单详情
     */
    @Override
    public Order findById(Integer id) {
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

    /**
     * 取消订单（删除）
     * 校验流程：
     * 1. 订单必须存在
     * 2. 必须是本人操作
     * 3. 订单状态不能为已完成(stage=1)
     * 4. 订单不能已支付(payTime不为空)
     * 5. 先删子表(order_item)再删主表(order)
     *
     * @param id 订单ID
     */
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