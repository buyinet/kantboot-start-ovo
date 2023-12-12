package com.kantboot.system.user.domain.dto;

import lombok.Data;

@Data
public class SysUserSaveDTOOfDtu {

    /**
     * 主键
     */
    private Long id;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 组织id
     */
    private Long orgId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 角色id
     */
    private String roleCode;

}
