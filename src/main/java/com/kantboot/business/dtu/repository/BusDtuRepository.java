package com.kantboot.business.dtu.repository;

import com.kantboot.business.dtu.domain.entity.BusDtu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusDtuRepository extends JpaRepository<BusDtu,Long> {

    /**
     * 根据orgId查询
     */
    List<BusDtu> findByOrgId(Long orgId);

    /**
     * 根据imei查询
     */
    BusDtu findByImei(String imei);


}
