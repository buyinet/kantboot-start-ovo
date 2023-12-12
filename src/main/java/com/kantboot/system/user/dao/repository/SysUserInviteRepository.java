package com.kantboot.system.user.dao.repository;

import com.kantboot.system.user.domain.entity.SysUserInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SysUserInviteRepository extends JpaRepository<SysUserInvite, Long> {

    /**
     * 添加邀请次数
     * @param userId 用户id
     */
    @Query("""
    UPDATE SysUserInvite
    SET inviteCount = inviteCount + 1
    WHERE userId = :userId
    """)
    void addInviteNum(Long userId);

    /**
     * 根据用户id获取邀请信息
     */
    SysUserInvite findByUserId(Long userId);

    /**
     * 添加间接邀请次数
     * @param userId 用户id
     */
    @Query("""
    UPDATE SysUserInvite
    SET inviteCountIndirect = inviteCountIndirect + 1
    WHERE userId = :userId
    """)
    void addInviteNumIndirect(Long userId);

}
