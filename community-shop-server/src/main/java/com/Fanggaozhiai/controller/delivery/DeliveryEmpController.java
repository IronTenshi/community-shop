package com.Fanggaozhiai.controller.delivery;

import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/delivery/emp")
public class DeliveryEmpController {
    @Autowired
    private DeliveryService deliveryService;
    //获得空闲订单列表
    //
    @GetMapping("/list")
    public Result list(){
        log.info("获得空闲订单列表");
        return Result.success(deliveryService.list());
    }

    //接单
    //delivery creating stage = 1
    //order stage = 0
    @PostMapping("/{ordId}")
    public Result accept(@PathVariable("ordId") Integer ordId,String note){
        log.info("接单id:{} 备注：{}",ordId,note);
        //判断空
        if (ordId == null){
            return Result.error("参数异常");
        }
        deliveryService.accept(ordId,note);
        return Result.success();
    }

    //配送完成
    //delivery stage = 2
    //order stage = 1
    @PutMapping("/{ordId}")
    public Result complete(@PathVariable("ordId") Integer ordId){
        log.info("配送完成id:{}",ordId);
        //判断空
        if (ordId == null){
            return Result.error("参数异常");
        }
        deliveryService.complete(ordId);
        return Result.success();
    }

    //获得我的订单
    @GetMapping("/my")
    public Result getMyOrder(){
        log.info("获得我的订单");
        return Result.success(deliveryService.getMyOrder());
    }
}
