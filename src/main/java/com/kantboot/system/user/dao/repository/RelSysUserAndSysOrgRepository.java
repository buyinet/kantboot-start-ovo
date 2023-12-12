package com.kantboot.system.user.dao.repository;

import com.kantboot.system.user.domain.entity.RelSysUserAndSysOrg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelSysUserAndSysOrgRepository extends JpaRepository<RelSysUserAndSysOrg,Long> {

    /**
     * 根据用户id获取组织
     */
    List<RelSysUserAndSysOrg> findByUserId(Long userId);


}
