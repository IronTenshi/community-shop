package com.Fanggaozhiai.controller.order;

import com.Fanggaozhiai.context.Context;
import com.Fanggaozhiai.dto.order.OrderParam;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 订单控制器
 * 提供用户下单、查询订单、取消订单等接口
 * 基础路径: /users/orders
 * 权限要求: 需要用户token认证（经过 UserPermissionFilter）
 */
@Slf4j
@RestController
@RequestMapping("/users/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 添加订单接口
     * 用户提交订单，包含商品明细列表
     * 后端自动设置创建时间和用户ID，不信任前端传值
     *
     * @param orderParam 订单参数，包含收货地址(reAddress)和订单明细列表(orderItems)
     * @return 操作结果
     */
    @PostMapping
    public Result add(@RequestBody OrderParam orderParam){
        Integer id = Context.getId();
        if(id == null){
            return Result.error("请先登录");
        }
        orderParam.setCreateTime(LocalDate.now());
        orderParam.setUsId(id);
        orderService.add(orderParam);
        return Result.success();
    }

    /**
     * 查询当前用户订单列表接口
     * 从ThreadLocal获取当前登录用户ID，查询该用户的所有订单
     *
     * @return 当前用户的订单列表
     */
    @GetMapping
    public Result list(){
        log.info("查询自己的订单");
        Integer id = Context.getId();
        if(id == null){
            return Result.error("请先登录");
        }
        return Result.success(orderService.list(id));
    }

    /**
     * 查询订单详情接口
     * 只能查询自己的订单详情（Service层做权限校验）
     *
     * @param id 订单ID
     * @return 订单详细信息
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable("id") Integer id){
        log.info("查询订单信息: {}", id);
        return Result.success(orderService.findById(id));
    }

    /**
     * 取消订单接口
     * 只能取消自己的订单，且订单状态必须为未支付(status=0)
     *
     * @param id 订单ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id){
        log.info("取消订单: {}", id);
        Integer usId = Context.getId();
        if(usId == null){
            return Result.error("请先登录");
        }
        orderService.delete(id);
        return Result.success();
    }
}