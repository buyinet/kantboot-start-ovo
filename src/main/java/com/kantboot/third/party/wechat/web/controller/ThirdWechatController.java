package com.kantboot.third.party.wechat.web.controller;

import com.kantboot.system.user.domain.vo.LoginVO;
import com.kantboot.third.party.wechat.service.ITpWechatService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/third-party-web/wechat")
public class ThirdWechatController {

    @Resource
    private ITpWechatService service;

    @RequestMapping("/loginInMp")
    public RestResult<LoginVO> loginInMp(@RequestParam("code") String code){
        return RestResult.success(service.loginInMp(code), "loginSuccess","登录成功");
    }



}
