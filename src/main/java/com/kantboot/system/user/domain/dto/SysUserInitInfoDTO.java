package com.kantboot.system.user.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class SysUserInitInfoDTO implements Serializable {

    /**
     * 性别编码
     */
    private String genderCode;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像的文件id
     */
    private Long fileIdOfAvatar;

    /**
     * 生日时间
     */
    private Date gmtBirthday;

    /**
     * 用户自我介绍
     */
    private String introduction;

    /**
     * sm倾向
     */
    private String sadomasochismCode;

    /**
     * 微信
     */
    private String wechat;

    /**
     * 邀请码
     */
    private String inviteCode;

}
