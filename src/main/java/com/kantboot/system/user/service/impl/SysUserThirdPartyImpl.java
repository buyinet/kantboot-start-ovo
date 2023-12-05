package com.kantboot.system.user.service.impl;

import com.kantboot.system.user.dao.repository.SysUserThirdPartyRepository;
import com.kantboot.system.user.domain.entity.SysUser;
import com.kantboot.system.user.domain.entity.SysUserThirdParty;
import com.kantboot.system.user.domain.vo.LoginVO;
import com.kantboot.system.user.service.ISysTokenService;
import com.kantboot.system.user.service.ISysUserService;
import com.kantboot.system.user.service.ISysUserThirdPartyService;
import com.kantboot.util.common.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserThirdPartyImpl implements ISysUserThirdPartyService {

    @Resource
    private SysUserThirdPartyRepository repository;
    @Resource
    private ISysTokenService tokenService;

    @Resource
    private ISysUserService userService;

    @Override
    public LoginVO login(SysUserThirdParty entity) {
        // 查询第三方用户
        SysUserThirdParty byThirdPartyCodeAndKeyAndValue = repository.findByThirdPartyCodeAndKeyAndValue(
                entity.getThirdPartyCode(),
                entity.getKey(),
                entity.getValue());
        // 如果没有则创建
        if(byThirdPartyCodeAndKeyAndValue == null){
            // 创建用户
            SysUser sysUser = userService.create(new SysUser().setIsTemporary(true));
            // 创建第三方用户
            SysUserThirdParty sysUserThirdParty = entity.setUserId(sysUser.getId());
            byThirdPartyCodeAndKeyAndValue = repository.save(sysUserThirdParty);
        }
        String token = tokenService.generateToken(byThirdPartyCodeAndKeyAndValue.getUserId());
        return new LoginVO()
                .setToken(token)
                .setUserInfo(userService.getSelfById(byThirdPartyCodeAndKeyAndValue.getUserId()));
    }

    @Override
    public SysUserThirdParty getByPlatformCodeAndKeyAndValue(String platformCode, String key, String value) {
        return repository.findByThirdPartyCodeAndKeyAndValue(platformCode, key, value);
    }

    @Override
    public void transferUserId(Long fromUserId, Long toUserId) {

        List<SysUserThirdParty> fromThird = repository.findByUserId(fromUserId);
        List<SysUserThirdParty> toThird = repository.findByUserId(toUserId);
        // 检查是否有双方都有的第三方用户
        for (SysUserThirdParty from : fromThird) {
            for (SysUserThirdParty to : toThird) {
                if(from.getThirdPartyCode().equals(to.getThirdPartyCode())&&from.getKey().equals(to.getKey())){
                    throw BaseException.of("thirdPartyConflict", "有重复绑定的第三方");
                }
            }
        }
        repository.updateUserIdByUserId(fromUserId, toUserId);
    }
}
