package com.Fanggaozhiai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 配送实体类
 * 对应数据库表: delivery
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {

    /** 配送ID，主键，自动递增 */
    private Integer id;

    /** 对应订单ID */
    private Integer ordId;

    /** 对应员工ID */
    private Integer empId;

    /** 状态: 0未派送 1正在派送 2已送达 */
    private Integer stage;

    /** 送达时间 */
    private LocalDate arrTime;

    /** 备注 */
    private String note;
}
