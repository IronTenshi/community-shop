package com.Fanggaozhiai.service.iml;

import com.Fanggaozhiai.context.Context;
import com.Fanggaozhiai.entity.Delivery;
import com.Fanggaozhiai.entity.Order;
import com.Fanggaozhiai.mapper.DeliveryMapper;
import com.Fanggaozhiai.mapper.EmployeeMapper;
import com.Fanggaozhiai.mapper.OrderMapper;
import com.Fanggaozhiai.service.DeliveryService;
import com.Fanggaozhiai.vo.DeliveryOrderReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
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
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private RedisTemplate redisTemplate;

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
     * 使用Redis SETNX分布式锁防止并发接单：
     * 多个配送员同时抢同一订单时，只有第一个成功SETNX的配送员能接单，其余直接返回失败
     * 锁key为 delivery:lock:{ordId}，30秒自动过期防止死锁
     *
     * @param ordId 订单ID
     * @param note  接单备注（可选）
     */
    @Override
    @Transactional
    public void accept(Integer ordId, String note) {
        Integer empId = Context.getId();
        String lockKey = "delivery:lock:" + ordId;

        // SETNX：只有 key 不存在时才能设置成功，返回 true 表示抢到锁
        Boolean locked = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, String.valueOf(empId), Duration.ofSeconds(30));

        if (Boolean.FALSE.equals(locked)) {
            throw new RuntimeException("该订单已被其他配送员接单");
        }

        try {
            // 二次检查数据库，防止锁过期后残留数据被误接
            Delivery existing = deliveryMapper.selectByOrdId(ordId);
            if (existing != null) {
                throw new RuntimeException("该订单已被其他配送员接单");
            }

            Delivery delivery = new Delivery();
            delivery.setEmpId(empId);
            delivery.setOrdId(ordId);
            delivery.setStage(1);
            delivery.setNote(note);
            //修改员工状态为1
            employeeMapper.getById(empId);
            //修改订单状态
            orderMapper.getById(ordId);
            deliveryMapper.addByDelivery(delivery);
        } catch (Exception e) {
            // 业务失败时释放锁，让其他配送员可以重试
            redisTemplate.delete(lockKey);
            throw e;
        }
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
        Integer id = Context.getId();
        //修改员工状态为0
        employeeMapper.completeById(id);
        deliveryMapper.complete(d, ordId);
        orderMapper.complete(ordId);
    }

    /**
     * 获取当前配送员的配送订单列表
     * 从ThreadLocal获取当前登录员工ID，查询其所有已接单的配送记录，并关联订单信息
     *
     * @return 配送订单信息列表
     */
    @Override
    public List<DeliveryOrderReturn> getMyOrder() {
        Integer id = Context.getId();
        List<Delivery> deliveries = deliveryMapper.getByEmpId(id);
        return deliveries.stream().map(d -> {
            Order order = orderMapper.selectById(d.getOrdId());
            return new DeliveryOrderReturn(d, order);
        }).toList();
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