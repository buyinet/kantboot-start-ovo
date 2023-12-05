package com.kantboot.third.party.wechat.service.impl;

import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.system.user.domain.entity.SysUserThirdParty;
import com.kantboot.system.user.domain.vo.LoginVO;
import com.kantboot.system.user.service.ISysUserThirdPartyService;
import com.kantboot.third.party.wechat.service.ITpWechatService;
import com.kantboot.third.party.wechat.util.mp.login.Code2Session;
import com.kantboot.util.common.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class TpWechatServiceImpl implements ITpWechatService {

    @Resource
    private ISysSettingService settingService;

    @Resource
    private ISysUserThirdPartyService thirdPartyService;

    private Code2Session.Result getCode2SessionByCode(String code){
        // 获取微信小程序appId
        String appid = settingService.getValue("wechat", "mpAppId");
        // 获取微信小程序appSecret
        String appSecret = settingService.getValue("wechat", "mpAppSecret");

        Code2Session.Param param = new Code2Session.Param();
        param.setAppid(appid);
        param.setSecret(appSecret);
        param.setJsCode(code);
        param.setGrantType("authorization_code");
        return Code2Session.getResult(param);
    }

    @Override
    public String getOpenIdByCodeInMp(String code) {
        return getCode2SessionByCode(code).getOpenid();
    }

    @Override
    public String getUnionIdByCodeInMp(String code) {
        return getCode2SessionByCode(code).getUnionid();
    }

    @Override
    public LoginVO loginInMp(String code) {
        Code2Session.Result code2Session = getCode2SessionByCode(code);
        SysUserThirdParty by = thirdPartyService.getByPlatformCodeAndKeyAndValue("wechat", "unionid", code2Session.getUnionid());
        if(by != null&&code2Session.getUnionid()!=null){
            return thirdPartyService.login(by);
        }
        by = thirdPartyService.getByPlatformCodeAndKeyAndValue("wechat", "openid", code2Session.getOpenid());
        if(by != null){
            return thirdPartyService.login(by);
        }
        SysUserThirdParty sysUserThirdParty = new SysUserThirdParty();
        if(code2Session.getUnionid()!=null) {
            sysUserThirdParty.setThirdPartyCode("wechat");
            sysUserThirdParty.setKey("unionid");
            sysUserThirdParty.setValue(code2Session.getUnionid());
            return thirdPartyService.login(sysUserThirdParty);
        }
        if(code2Session.getOpenid()!=null) {
            sysUserThirdParty.setThirdPartyCode("wechat");
            sysUserThirdParty.setKey("openid");
            sysUserThirdParty.setValue(code2Session.getOpenid());
            return thirdPartyService.login(sysUserThirdParty);
        }
        throw BaseException.of("loginFail","登录失败");
    }
}
