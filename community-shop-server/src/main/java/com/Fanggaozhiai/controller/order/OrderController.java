package com.Fanggaozhiai.controller.order;

import com.Fanggaozhiai.context.Context;
import com.Fanggaozhiai.dto.OrderParam;
import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/users/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    //添加订单
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
    //查询自己的订单
    @GetMapping
    public Result list(){
        log.info("查询自己的订单");
        Integer id = Context.getId();
        if(id == null){
            return Result.error("请先登录");
        }
        return Result.success(orderService.list(id));
    }
    //查看订单详细
    //只能查询自己的订单详细
    @GetMapping("/{id}")
    public Result findById(@PathVariable("id") Integer id){
        log.info("查询订单信息: {}", id);
        return Result.success(orderService.findById(id));
    }
    //取消订单
    //订单属于用户且未支付status = 0
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id){
        log.info("取消订单: {}", id);
        //判断是否为用户本人操作
        Integer usId = Context.getId();
        if(usId == null){
            return Result.error("请先登录");
        }
        orderService.delete(id);
        return Result.success();
    }
}
