package com.kantboot.business.dtu.repository;

import com.kantboot.business.dtu.domain.entity.BusDtuStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusDtuStatusRepository extends JpaRepository<BusDtuStatus,Long> {

    /**
     * 根据imei查询
     */
    BusDtuStatus findByImei(String imei);

}
