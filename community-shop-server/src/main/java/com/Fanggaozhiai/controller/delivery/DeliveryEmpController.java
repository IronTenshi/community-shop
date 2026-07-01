package com.Fanggaozhiai.controller.delivery;

import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 配送员工控制器
 * 提供配送员接单、完成配送、查看个人配送订单等接口
 * 基础路径: /delivery/emp
 * 权限要求: 需要员工token认证，且当前员工ID必须与操作匹配（经过 EmployeePermissionFilter）
 */
@Slf4j
@RestController
@RequestMapping("/delivery/emp")
public class DeliveryEmpController {
    @Autowired
    private DeliveryService deliveryService;

    /**
     * 获取空闲订单列表接口
     * 返回所有未被接单的订单（order.stage = 0），供配送员选择接单
     *
     * @return 空闲订单列表
     */
    @GetMapping("/list")
    public Result list(){
        log.info("获得空闲订单列表");
        return Result.success(deliveryService.list());
    }

    /**
     * 接单接口
     * 配送员接单后，创建配送记录（delivery.stage = 1 配送中），订单状态保持为0（未完成）
     *
     * @param ordId 订单ID
     * @param note  接单备注（可选）
     * @return 操作结果
     */
    @PostMapping("/{ordId}")
    public Result accept(@PathVariable("ordId") Integer ordId, @RequestParam(required = false) String note){
        log.info("接单id:{} 备注：{}", ordId, note);
        if (ordId == null){
            return Result.error("参数异常");
        }
        try {
            deliveryService.accept(ordId, note);
            return Result.success();
        } catch (RuntimeException e) {
            log.warn("接单失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 完成配送接口
     * 配送员确认送达后，更新配送状态为已送达（delivery.stage = 2），订单状态更新为已完成（order.stage = 1）
     *
     * @param ordId 订单ID
     * @return 操作结果
     */
    @PutMapping("/{ordId}")
    public Result complete(@PathVariable("ordId") Integer ordId){
        log.info("配送完成id:{}", ordId);
        if (ordId == null){
            return Result.error("参数异常");
        }
        deliveryService.complete(ordId);
        return Result.success();
    }

    /**
     * 获取我的配送订单接口
     * 返回当前登录配送员已接单的配送记录（含订单信息和配送信息）
     *
     * @return 当前配送员的配送订单列表
     */
    @GetMapping("/my")
    public Result getMyOrder(){
        log.info("获得我的订单");
        return Result.success(deliveryService.getMyOrder());
    }
}