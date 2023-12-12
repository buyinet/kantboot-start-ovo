package com.kantboot.api.lbs.repository;

import com.kantboot.api.lbs.domain.vo.ApiLbsAdHasAdlvVO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiLbsAdHasAdlvVORepository extends JpaRepository<ApiLbsAdHasAdlvVO, Long>  {

    /**
     * 根据adcode查询
     */
    ApiLbsAdHasAdlvVO findByAdcode(String adcode);
}
