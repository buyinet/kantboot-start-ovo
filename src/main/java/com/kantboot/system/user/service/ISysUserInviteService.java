package com.kantboot.system.user.service;

public interface ISysUserInviteService {

    /**
     * 添加邀请次数
     * @param inviteUserId 邀请人id
     */
    void addInviteNum(Long inviteUserId);

}
