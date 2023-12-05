package com.kantboot.api.varcode.service;

import com.kantboot.api.varcode.domain.entity.ApiVarCode;

public interface IApiVarCodeService {

    String generateCode();

    void send(ApiVarCode apiVarCode);

    void sendSms(ApiVarCode apiVarCode);

    void sendEmail(ApiVarCode apiVarCode);

    /**
     * 判断邮箱验证码是否正确
     */
    Boolean checkEmailCode(ApiVarCode apiVarCode);


    /**
     * 判断短信验证码是否正确
     */
    Boolean checkSmsCode(ApiVarCode apiVarCode);

}
