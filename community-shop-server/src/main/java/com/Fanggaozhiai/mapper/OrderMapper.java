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

    /**
     * 查询空闲订单（stage = 0）
     * 供配送员查看可接单的订单列表
     *
     * @return 空闲订单列表
     */
    @Select("select * from `order` where stage = 0")
    List<Order> listByStage();

    /**
     * 完成订单
     * 配送员送达后，将订单状态更新为已完成（stage = 1）
     *
     * @param id 订单ID
     */
    @Update("update `order` set stage = 2 where id = #{id}")
    void complete(Integer id);

    /**
     * 查询所有订单（管理员用）
     *
     * @return 所有订单列表
     */
    @Select("select * from `order`")
    List<Order> listAll();

    /**
     * 根据订单ID查询订单（管理员用）
     *
     * @param ordId 订单ID
     * @return 订单实体
     */
    @Update("update `order` set stage = 1 where id = #{ordId}")
    void getById(Integer ordId);
}