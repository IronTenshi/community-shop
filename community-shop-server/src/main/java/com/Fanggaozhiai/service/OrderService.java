package com.Fanggaozhiai.service;

import com.Fanggaozhiai.dto.order.OrderParam;
import com.Fanggaozhiai.entity.Order;

import java.util.List;

public interface OrderService {
    void add(OrderParam orderParam);

    List<Order> list(Integer id);

    Order findById(Integer id);

    void delete(Integer id);
}
