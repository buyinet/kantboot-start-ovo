package com.kantboot.business.ac.controller;

import com.kantboot.business.ac.service.IBusAcCommonService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/business-ac-web/ac/common")
public class BusAcCommonController {

    @Resource
    private IBusAcCommonService service;

    @RequestMapping("/getAcProjectSetting")
    public RestResult<Map<String,String>> getAcProjectSetting() {
        return RestResult.success(service.getAcProjectSetting(),"getSuccess","获取成功");
    }

    /**
     * 计算自己的积分余额可以兑换多少人民币
     */
    @RequestMapping("/getSelfBalanceScoreToRmb")
    public RestResult<Double> getSelfBalanceScoreToRmb() {
        return RestResult.success(service.getSelfBalanceScoreToRmb(),"getSuccess","获取成功");
    }

}
