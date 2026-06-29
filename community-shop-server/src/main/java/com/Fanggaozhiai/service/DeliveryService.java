package com.Fanggaozhiai.service;

import com.Fanggaozhiai.entity.Order;
import com.Fanggaozhiai.vo.DeliveryOrderReturn;

import java.util.List;

public interface DeliveryService {
    //获取空闲订单
    List<Order> list();

    //接单
    void accept(Integer ordId,String note);

    //完成接单
    void complete(Integer ordId);

    Order getMyOrder();

    List<Order> selectAll();
    //返回订单和delivery详细订单
    DeliveryOrderReturn selectById(Integer id);
}
