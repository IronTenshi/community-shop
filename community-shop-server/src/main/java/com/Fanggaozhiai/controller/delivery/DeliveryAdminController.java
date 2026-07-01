package com.Fanggaozhiai.controller.delivery;

import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配送管理控制器（管理员专属）
 * 提供管理员查看所有订单及配送详情的接口
 * 基础路径: /delivery/admin
 * 权限要求: 需要管理员token认证（经过 EmployeePermissionFilter）
 */
@Slf4j
@RestController
@RequestMapping("/delivery/admin")
public class DeliveryAdminController {
    @Autowired
    private DeliveryService deliveryService;

    /**
     * 查看所有订单接口
     * 管理员可以查看系统中所有订单（含配送状态）
     *
     * @return 所有订单列表
     */
    @GetMapping("/list")
    public Result listAll(){
        log.info("获得所有订单");
        return Result.success(deliveryService.selectAll());
    }

    /**
     * 查看订单详情及配送信息接口
     * 根据订单ID返回订单信息和对应的配送记录（order + delivery）
     *
     * @param id 订单ID
     * @return 订单详情及配送信息
     */
    @GetMapping("/{id}")
    public Result selectById(@PathVariable("id") Integer id){
        log.info("订单id : {}", id);
        if (id == null){
            return Result.error("参数异常");
        }
        return Result.success(deliveryService.selectById(id));
    }
}