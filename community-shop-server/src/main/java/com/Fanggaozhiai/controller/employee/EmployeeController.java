package com.Fanggaozhiai.controller.employee;

import com.Fanggaozhiai.dto.UserLoginDTO;
import com.Fanggaozhiai.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmployeeController {
    //根据员工username 和 password 登录
    @PostMapping
    public Result login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("员工登录: {}", userLoginDTO);
        return Result.success();
    }
}
