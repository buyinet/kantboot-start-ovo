package com.kantboot.system.user.service;

import com.kantboot.system.user.domain.entity.SysUserThirdParty;
import com.kantboot.system.user.domain.vo.LoginVO;

public interface ISysUserThirdPartyService {

    /**
     * 登录
     */
    LoginVO login(SysUserThirdParty entity);

    /**
     * 根据平台编码、key、value获取用户信息
     */
    SysUserThirdParty getByPlatformCodeAndKeyAndValue(String platformCode, String key, String value);

    /**
     * 将userId转换到另一个userId
     */
    void transferUserId(Long fromUserId, Long toUserId);


}
