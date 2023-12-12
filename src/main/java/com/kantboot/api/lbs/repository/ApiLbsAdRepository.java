package com.kantboot.api.lbs.repository;

import com.kantboot.api.lbs.domain.entity.ApiLbsAd;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiLbsAdRepository extends JpaRepository<ApiLbsAd, Long> {

    /**
     * 根据adcode查询
     */
    ApiLbsAd findByAdcode(String adcode);

}
