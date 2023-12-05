package com.kantboot.api.email.phone.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.util.common.exception.BaseException;

import java.util.Map;

public class SmsUtil {

    /**
     * 发送短信
     * 北京深智恒际科技有限公司
     * https://market.aliyun.com/apimarket/detail/cmapi00037170?spm=5176.2020520132.101.21.77fb7218HWiC1I
     * @param phone 手机号
     * @param content 内容
     * @param templateId 模板id
     * @param appcode appcode
     */
    public static void dfsnsSendOfAlicloud(String phone, String content, String templateId, String appcode) {
        Map<String,Object> map = Map.of("phone_number",phone,"content",content,"template_id",templateId);
        String authorization = HttpUtil
                .createPost("https://dfsns.market.alicloudapi.com/data/send_sms")
                .contentType("application/x-www-form-urlencoded;charset=utf-8")
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .header("Authorization", "APPCODE "+appcode)
                .form(map)
                .execute()
                .body();
        System.out.println(authorization);
        if (authorization.indexOf("Error request, response status: 403")!= -1) {
             throw BaseException.of("smsNotPay","短信服务暂时停止");
        }
        JSONObject jsonObject = JSON.parseObject(authorization);
        if ("RATE_LIMIT".equals(jsonObject.get("status"))) {
            throw BaseException.of("smsRateLimit","此号码短信发送频率过高");
        }

    }


    public static void main(String[] args) {
        dfsnsSendOfAlicloud("13155908637","code:1234","CST_ptdie100","055272a7524a4a5baec910d494451a19");
    }

}
