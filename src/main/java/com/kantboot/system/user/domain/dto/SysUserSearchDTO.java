package com.kantboot.system.user.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserSearchDTO implements Serializable {

    private String username;

    private String phone;

    private String email;

    private String nickname;

    private String genderCode;


}
