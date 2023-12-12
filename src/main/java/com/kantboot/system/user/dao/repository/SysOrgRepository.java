package com.kantboot.system.user.dao.repository;

import com.kantboot.system.user.domain.dto.SysOrgSearchDTO;
import com.kantboot.system.user.domain.entity.SysOrg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SysOrgRepository extends JpaRepository<SysOrg,Long> {


    @Query("""
    FROM SysOrg
    WHERE
        (:#{#param.id} IS NULL OR id = :#{#param.id})
        AND (:#{#param.name} IS NULL OR :#{#param.name} = '' OR name LIKE CONCAT('%',:#{#param.name},'%'))
    """)
    Page<SysOrg> getBodyData(@Param("param")SysOrgSearchDTO param, Pageable pageable);

}
