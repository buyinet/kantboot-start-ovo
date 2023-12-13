package com.kantboot.system.user.dao.repository;

import com.kantboot.system.user.domain.entity.SysUserBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysUserBalanceRepository extends JpaRepository<SysUserBalance,Long> {

    /**
     * 根据用户id获取所有余额
     */
    List<SysUserBalance> findByUserId(Long userId);

    /**
     * 根据用户id和balanceCode获取余额
     */
    SysUserBalance findByUserIdAndBalanceCode(Long userId, String balanceCode);

}
