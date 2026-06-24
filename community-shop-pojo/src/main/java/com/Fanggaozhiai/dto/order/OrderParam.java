package com.Fanggaozhiai.dto.order;

import com.Fanggaozhiai.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderParam {
    private Integer id;
    private Integer usId;
    private double money;
    private String reAddress;
    private LocalDate createTime;
    private List<OrderItem> orderItems;
}
