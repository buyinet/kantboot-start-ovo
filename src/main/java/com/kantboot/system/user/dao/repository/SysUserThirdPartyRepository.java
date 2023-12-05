package com.kantboot.system.user.dao.repository;

import com.kantboot.system.user.domain.entity.SysUserThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysUserThirdPartyRepository extends JpaRepository<SysUserThirdParty, Long> {

    /**
     * 查询第三方用户
     */
    SysUserThirdParty findByThirdPartyCodeAndKeyAndValue(String thirdPartyCode, String key, String value);

    /**
     * 根据userId转换
     */
    @Query("UPDATE SysUserThirdParty t SET t.userId = ?2 WHERE t.userId = ?1")
    void updateUserIdByUserId(Long fromUserId, Long toUserId);

    /**
     * 根据用户id搜索全部
     */
    List<SysUserThirdParty> findByUserId(Long userId);

}
