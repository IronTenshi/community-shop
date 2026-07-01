package com.Fanggaozhiai.mapper;

import com.Fanggaozhiai.entity.Delivery;
import com.Fanggaozhiai.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;

/**
 * 配送数据访问层接口
 * 负责配送相关的数据库操作
 */
@Mapper
public interface DeliveryMapper {

    /**
     * 新增配送记录
     * 配送员接单时创建，stage默认为1（配送中）
     *
     * @param delivery 配送实体，包含 ordId、empId、stage、note
     */
    @Insert("insert into delivery(ord_id, emp_id, stage, note) values(#{ordId},#{empId},#{0},#{note})")
    void addByDelivery(Delivery delivery);

    /**
     * 完成配送
     * 更新配送状态为2（已送达），并记录送达时间
     *
     * @param localDate 送达时间
     * @param ordId     订单ID
     */
    @Update("update delivery set stage = 2 ,arr_time = #{localDate} where ord_id = #{ordId}")
    void complete(LocalDate localDate, Integer ordId);

    /**
     * 根据员工ID查询配送记录
     * 查询当前配送员已接单的配送记录
     *
     * @param empId 员工ID
     * @return 配送对应的订单信息
     */
    @Select("select * from delivery where emp_id = #{empId}")
    Order getByEmpId(Integer empId);

    /**
     * 根据订单ID查询配送记录
     * 用于查询订单的配送详情
     *
     * @param ordId 订单ID
     * @return 配送实体
     */
    @Select("select * from delivery where ord_id = #{ordId}")
    Delivery selectByOrdId(Integer ordId);
}