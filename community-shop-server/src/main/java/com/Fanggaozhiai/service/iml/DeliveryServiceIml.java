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

@Service
public class DeliveryServiceIml implements DeliveryService {
    @Autowired
    private DeliveryMapper deliveryMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<Order> list() {
        //返回订单(空闲)
        //filter 拦截
        //只select中的stage = 0 的订单
        return orderMapper.listByStage();
    }

    @Override
    public void accept(Integer ordId,String note) {
        //获取上下文, get from threadlocal
        Integer usId =  Context.getId();
        //orderid 创建delivery
        Delivery delivery = new Delivery();
        delivery.setEmpId(usId);
        delivery.setOrdId(ordId);
        delivery.setStage(1);
        delivery.setNote(note);
        //进行sql 操作
        deliveryMapper.addByDelivery(delivery);
    }

    //完成订单
    //2 次sql
    @Override
    @Transactional
    public void complete(Integer ordId) {
//        //获取上下文usId
//        Integer Id = Context.getId();
        //开启事务
        //不用校验
        //填写送达时间
        //修改delivery 中的 stage = 2 为送达成功
        LocalDate d = LocalDate.now();
        deliveryMapper.complete(d,ordId);
        //修改order 中的 stage = 1 为送达成功
        orderMapper.complete(ordId);
    }

    //
    @Override
    public Order getMyOrder() {
        //获取上下文id
        Integer id =  Context.getId();
        //通过empid来查询delivery
        return deliveryMapper.getByEmpId(id);
    }

    @Override
    public List<Order> selectAll() {
        return orderMapper.listAll();
    }

    @Override
    @Transactional
    public DeliveryOrderReturn selectById(Integer id) {
        Order order = orderMapper.selectById(id);
        Delivery delivery = deliveryMapper.selectByOrdId(id);
        return new DeliveryOrderReturn(delivery,order);
    }
}
