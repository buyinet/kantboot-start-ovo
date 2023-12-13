package com.kantboot.system.user.web.controller;

import com.kantboot.system.user.domain.entity.SysBalance;
import com.kantboot.system.user.service.ISysBalanceService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system-user-web/balance")
public class SysBalanceController {

    @Resource
    private ISysBalanceService service;

    @RequestMapping("/getAll")
    public RestResult<List<SysBalance>> getAll() {
        return RestResult.success(service.getAll(),"getSuccess","获取成功");
    }

    @RequestMapping("/getSelf")
    public RestResult<Map<String,Double>> getSelf() {
        return RestResult.success(service.getSelf(),"getSuccess","获取成功");
    }

}
