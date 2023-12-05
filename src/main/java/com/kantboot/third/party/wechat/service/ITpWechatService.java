package com.kantboot.third.party.wechat.service;

import com.kantboot.system.user.domain.vo.LoginVO;

/**
 * 第三方微信用户服务
 * @author 方某方
 */
public interface ITpWechatService {

    /**
     * 在微信小程序中通过code获取openid
     * @param code 微信端获取到的code
     * @return openid
     */
    String getOpenIdByCodeInMp(String code);

    /**
     * 在微信小程序中通过code获取unionid
     * @param code 微信端获取到的code
     * @return unionid
     */
    String getUnionIdByCodeInMp(String code);

    /**
     * 微信小程序登录
     */
    LoginVO loginInMp(String code);

}
