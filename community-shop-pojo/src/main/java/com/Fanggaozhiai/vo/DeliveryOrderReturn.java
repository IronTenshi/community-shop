package com.Fanggaozhiai.vo;

import com.Fanggaozhiai.entity.Delivery;
import com.Fanggaozhiai.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryOrderReturn {
    private Delivery delivery;
    private Order order;
}
