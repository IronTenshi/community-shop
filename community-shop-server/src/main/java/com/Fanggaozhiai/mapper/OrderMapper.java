package com.Fanggaozhiai.mapper;

import com.Fanggaozhiai.dto.order.OrderParam;
import com.Fanggaozhiai.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 订单数据访问层接口
 * 负责订单相关的数据库操作
 */
@Mapper
public interface OrderMapper {

    /**
     * 新增订单
     * 使用useGeneratedKeys自动回填自增主键id到orderParam对象
     *
     * @param orderParam 订单参数
     */
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    @Insert("insert into `order`(us_id, money, re_address, create_time) values(#{usId}, #{money}, #{reAddress}, #{createTime})")
    void add(OrderParam orderParam);

    /**
     * 根据用户ID查询订单列表
     *
     * @param id 用户ID
     * @return 订单列表
     */
    @Select("select * from `order` where us_id = #{id}")
    List<Order> list(Integer id);

    /**
     * 根据ID查询订单
     *
     * @param id 订单ID
     * @return 订单实体
     */
    @Select("select * from `order` where id = #{id}")
    Order selectById(Integer id);

    /**
     * 根据ID删除订单
     *
     * @param id 订单ID
     */
    @Delete("delete from `order` where id = #{id}")
    void delete(Integer id);
}