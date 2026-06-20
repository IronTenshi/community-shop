package com.Fanggaozhiai.mapper;

import com.Fanggaozhiai.dto.OrderParam;
import com.Fanggaozhiai.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    @Insert("insert into `order`(us_id, money, re_address, create_time) values(#{usId}, #{money}, #{reAddress}, #{createTime})")
    void add(OrderParam orderParam);

    @Select("select * from `order` where us_id = #{id}")
    List<Order> list(Integer id);

    @Select("select * from `order` where id = #{id}")
    Order selectById(Integer id);

    @Delete("delete from `order` where id = #{id}")
    void delete(Integer id);
}
