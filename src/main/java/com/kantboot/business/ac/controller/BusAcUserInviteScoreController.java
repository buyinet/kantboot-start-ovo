package com.kantboot.business.ac.controller;

import com.kantboot.business.ac.service.IBusAcUserInviteScoreService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/business-ac-web/ac/userInviteScore")
public class BusAcUserInviteScoreController {

    @Resource
    private IBusAcUserInviteScoreService service;

    /**
     * 获取自己的邀请积分
     */
    @RequestMapping("/getSelfScoreOfInvite")
    public RestResult<BigDecimal> getSelfScoreOfInvite() {
        return RestResult.success(service.getSelfScoreOfInvite(),"getSuccess","获取成功");
    }

}
