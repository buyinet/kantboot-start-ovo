package com.kantboot.system.user.dao.repository;

import com.kantboot.system.user.domain.dto.SysUserSearchDTO;
import com.kantboot.system.user.domain.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 用户DAO
 * @author 方某方
 */
public interface SysUserRepository extends JpaRepository<SysUser, Long> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    SysUser findByUsername(String username);

    /**
     * 根据手机号码查询用户
     * @param phone 手机号码
     * @return 用户
     */
    SysUser findByPhone(String phone);

    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户
     */
    SysUser findByEmail(String email);

    @Query("""
    FROM SysUser t
    WHERE (:#{#param.username} IS NULL OR t.username LIKE CONCAT('%',:#{#param.username},'%') OR :#{#param.username} = '')
        AND (:#{#param.phone} IS NULL OR t.phone LIKE CONCAT('%',:#{#param.phone},'%') OR :#{#param.phone} = '')
        AND (:#{#param.email} IS NULL OR t.email LIKE CONCAT('%',:#{#param.email},'%') OR :#{#param.email} = '')
        AND (:#{#param.nickname} IS NULL OR t.nickname LIKE CONCAT('%',:#{#param.nickname},'%') OR :#{#param.nickname} = '')
        AND (:#{#param.genderCode} IS NULL OR t.genderCode = :#{#param.genderCode})
    """)
    Page<SysUser> getBodyData(@Param("param") SysUserSearchDTO param, Pageable pageable);

    /**
     * 根据直属码查询用户
     */
    SysUser findByDirectCode(String directCode);

}
