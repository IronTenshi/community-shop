package com.Fanggaozhiai.controller.delivery;

import com.Fanggaozhiai.result.Result;
import com.Fanggaozhiai.service.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/delivery/admin")
public class DeliveryAdminController {
    @Autowired
    private DeliveryService deliveryService;
    //查看所有订单
    @GetMapping("/list")
    public Result listAll(){
        log.info("获得所有订单");
        return Result.success(deliveryService.selectAll());
    }

    //查看订单详细和配送信息
    //order + delivery
    @GetMapping("/{id}")
    public Result selectById(@PathVariable("id")Integer id){
        log.info("订单id : {}",id);
        //判断空
        if (id == null){
            return Result.error("参数异常");
        }
        return Result.success(deliveryService.selectById(id));
    }
}
