package com.Fanggaozhiai.mapper;

import com.Fanggaozhiai.entity.OrderItem;
import jakarta.websocket.server.PathParam;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单明细数据访问层接口
 * 负责订单明细相关的数据库操作
 * 对应的XML映射文件位于：resources/com/Fanggaozhiai/mapper/OrderItemMapper.xml
 */
@Mapper
public interface OrderItemMapper {

    /**
     * 批量新增订单明细
     * 使用foreach批量插入，一次插入多条记录
     *
     * @param orderItems 订单明细列表
     */
    void add(@Param("orderItems") List<OrderItem> orderItems);

    /**
     * 根据订单ID删除所有明细
     * 用于取消订单时级联删除
     *
     * @param id 订单ID
     */
    @Delete("delete from order_item where ord_id = #{id}")
    void delete(Integer id);
}