package com.Fanggaozhiai.mapper;

import com.Fanggaozhiai.entity.Delivery;
import com.Fanggaozhiai.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;

@Mapper
public interface DeliveryMapper {
    @Insert("insert into delivery(ord_id, emp_id, stage, note) values(#{ordId},#{empId},#{0},#{note})")
    void addByDelivery(Delivery delivery);

    @Update("update delivery set stage = 2 ,arr_time = #{localDate} where ord_id = #{ordId}")
    void complete(LocalDate localDate,Integer ordId);

    @Select("select * from delivery where emp_id = #{empId}")
    Order getByEmpId(Integer empId);

    @Select("select * from delivery where ord_id = #{ordId}")
    Delivery selectByOrdId(Integer ordId);
}
