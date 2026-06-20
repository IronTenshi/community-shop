package com.Fanggaozhiai.mapper;

import com.Fanggaozhiai.entity.OrderItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    void add(List<OrderItem> orderItems);

    @Delete("delete from order_item where ord_id = #{id}")
    void delete(Integer id);
}
