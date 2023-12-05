package com.kantboot.api.varcode.service.impl;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.kantboot.api.email.phone.util.SmsUtil;
import com.kantboot.api.varcode.domain.entity.ApiVarCode;
import com.kantboot.api.varcode.service.IApiVarCodeService;
import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.util.common.exception.BaseException;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class ApiVarCodeServiceImpl implements IApiVarCodeService {

    @Resource
    private ISysSettingService settingService;

    @Resource
    private RedisUtil redisUtil;


    @Override
    public String generateCode() {
        // 生成6位数字验证码
        String code = "";
        for (int i = 0; i < 6; i++) {
            code += (int) (Math.random() * 10);
        }
        return code;
    }

    @Override
    public void send(ApiVarCode apiVarCode) {
        if (ApiVarCode.TYPE_CODE_SMS.equals(apiVarCode.getTypeCode())) {
            sendSms(apiVarCode);
        } else if (ApiVarCode.TYPE_CODE_EMAIL.equals(apiVarCode.getTypeCode())) {
            sendEmail(apiVarCode);
        }
    }

    @Override
    public void sendSms(ApiVarCode apiVarCode) {
        HashMap<String, String> sms = settingService.getMapByGroupCode("sms");
        String phone = apiVarCode.getTo();
        String code = generateCode();
        // 如果开头是+86
        if (phone.startsWith("+86")) {
            String templateId = sms.get("templateIdOfDfsns");
            String appcodeOfAlicloud = sms.get("appcodeOfAlicloud");
            phone = phone.substring(3);
            SmsUtil.dfsnsSendOfAlicloud(phone, "code:"+code,templateId,appcodeOfAlicloud);
        }
        else if (phone.startsWith("+")) {
            // 暂不支持，该地区的SMS服务
            throw BaseException.of("smsNotSupport","暂不支持该地区的SMS服务");
        }

        // 保存到redis，30分钟有效
        redisUtil.setEx("smsCode:" + apiVarCode.getSceneCode() + ":" + apiVarCode.getTo(), code, 30, TimeUnit.MINUTES);
    }

    @Override
    public void sendEmail(ApiVarCode apiVarCode) {
        HashMap<String, String> smtp = settingService.getMapByGroupCode("smtp");
        MailAccount account = new MailAccount();
        account.setHost(smtp.get("host"));
        account.setPort(25);
        account.setAuth(true);
        account.setFrom(smtp.get("email"));
        account.setUser(smtp.get("user"));
        account.setPass(smtp.get("password"));
        String email = apiVarCode.getTo();
        String code = generateCode();
        // 保存到redis，30分钟有效
        redisUtil.setEx("emailCode:" + apiVarCode.getSceneCode() + ":" + email, code, 30, TimeUnit.MINUTES);

        MailUtil.send(account, email,
                "验证码",
                "[OVO]您的验证码是：<span style='font-weight:bold'>" + code + "</span>，请在5分钟内完成验证。如非本人操作，请忽略。",
                true);
    }

    @Override
    public Boolean checkEmailCode(ApiVarCode apiVarCode) {
        String value = redisUtil.get("emailCode:" + apiVarCode.getSceneCode() + ":" + apiVarCode.getTo());
        if(apiVarCode.getValue().equals(value)){
            redisUtil.delete("emailCode:" + apiVarCode.getSceneCode() + ":" + apiVarCode.getTo());
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkSmsCode(ApiVarCode apiVarCode) {
        String value = redisUtil.get("smsCode:" + apiVarCode.getSceneCode() + ":" + apiVarCode.getTo());
        if(apiVarCode.getValue().equals(value)){
            redisUtil.delete("smsCode:" + apiVarCode.getSceneCode() + ":" + apiVarCode.getTo());
            return true;
        }
        return false;
    }
}
