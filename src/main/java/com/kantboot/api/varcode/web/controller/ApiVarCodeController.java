package com.kantboot.api.varcode.web.controller;

import com.kantboot.api.varcode.domain.entity.ApiVarCode;
import com.kantboot.api.varcode.service.IApiVarCodeService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-varcode/varCode")
public class ApiVarCodeController {

    @Resource
    private IApiVarCodeService service;

    /**
     * 发送验证码
     * @return 结果
     */
    @RequestMapping("/send")
    public RestResult send(@RequestBody ApiVarCode apiVarCode) {
        service.send(apiVarCode);
        return RestResult.success(null,"sendSuccess","发送成功");
    }

    /**
     * 发送邮箱验证码
     * @return 结果
     */
    @RequestMapping("/sendEmail")
    public RestResult sendEmail(@RequestBody ApiVarCode apiVarCode) {
        service.sendEmail(apiVarCode);
        return RestResult.success(null,"sendSuccess","发送成功");
    }

    /**
     * 发送短信验证码
     */
    @RequestMapping("/sendSms")
    public RestResult sendSms(@RequestBody ApiVarCode apiVarCode) {
        service.sendSms(apiVarCode);
        return RestResult.success(null,"sendSuccess","发送成功");
    }

    /**
     * 判断验证码是否正确
     */
    @RequestMapping("/checkEmailCode")
    public RestResult<Boolean> checkEmailCode(@RequestBody ApiVarCode apiVarCode) {
        return RestResult.success(service.checkEmailCode(apiVarCode), "getSuccess","获取成功");
    }

}
