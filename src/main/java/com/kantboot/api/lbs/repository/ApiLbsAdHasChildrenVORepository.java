package com.kantboot.api.lbs.repository;

import com.kantboot.api.lbs.domain.vo.ApiLbsAdHasChildrenVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApiLbsAdHasChildrenVORepository extends JpaRepository<ApiLbsAdHasChildrenVO, Long> {

    @Query("""
           FROM ApiLbsAdHasChildrenVO
           WHERE parentAdcode = null OR parentAdcode = ''
           ORDER BY id ASC""")
    List<ApiLbsAdHasChildrenVO> findAll();

}
