package com.kantboot.system.user.service.impl;

import com.kantboot.system.user.dao.repository.SysUserInviteRepository;
import com.kantboot.system.user.dao.repository.SysUserRepository;
import com.kantboot.system.user.domain.entity.SysUserInvite;
import com.kantboot.system.user.service.ISysUserInviteService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SysUserInviteServiceImpl implements ISysUserInviteService {

    @Resource
    private SysUserInviteRepository repository;

    @Resource
    private SysUserRepository userRepository;

    @Override
    public void addInviteNum(Long inviteUserId) {
        SysUserInvite byUserId = repository.findByUserId(inviteUserId);
        if(byUserId == null){
            SysUserInvite sysUserInvite = new SysUserInvite();
            sysUserInvite.setId(inviteUserId);
            sysUserInvite.setUserId(inviteUserId);
            sysUserInvite.setInviteCount(0L);
            sysUserInvite.setInviteCountIndirect(0L);
            repository.save(sysUserInvite);
            repository.flush();
        }
        SysUserInvite byUserId1 = repository.findByUserId(inviteUserId);
        byUserId1.setInviteCount(byUserId1.getInviteCount() + 1);
        userRepository.findById(inviteUserId).ifPresent(user -> {
            if(user.getInviteUserId() != null){
                SysUserInvite byUserId2 = repository.findByUserId(user.getInviteUserId());
                if(byUserId2 == null){
                    SysUserInvite sysUserInvite = new SysUserInvite();
                    sysUserInvite.setId(user.getInviteUserId());
                    sysUserInvite.setUserId(user.getInviteUserId());
                    sysUserInvite.setInviteCount(0L);
                    sysUserInvite.setInviteCountIndirect(0L);
                    repository.save(sysUserInvite);
                    repository.flush();
                }
                if(byUserId2.getInviteCountIndirect()==null){
                    byUserId2.setInviteCountIndirect(0L);
                }
                byUserId2.setInviteCountIndirect(byUserId2.getInviteCountIndirect() + 1);
                repository.save(byUserId2);
            }
        });
    }
}
