package com.kantboot.system.user.web.admin.controller;

import com.kantboot.system.user.service.ISysBalanceService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system-user-web/admin/balance")
public class SysBalanceControllerOfAdmin {

    @Resource
    private ISysBalanceService service;

    @RequestMapping("/getAll")
    public RestResult getAll() {
        return RestResult.success(service.getAll(),"getSuccess","获取成功");
    }

    @RequestMapping("addBalance")
    public RestResult addBalance(
            @RequestParam("userId")
            Long userId,
            @RequestParam("balanceCode")
            String balanceCode,
            @RequestParam("number")
            Double number) {
        return RestResult.success(service.addBalance(userId,balanceCode,number),"addSuccess","添加成功");
    }

}
