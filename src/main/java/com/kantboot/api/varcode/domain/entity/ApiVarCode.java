package com.kantboot.api.varcode.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

@Table(name="api_var_code")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class ApiVarCode implements Serializable {

    /**
     * 类型编码
     * 短信验证码
     */
    public static final String TYPE_CODE_SMS = "sms";

    /**
     * 类型编码
     * 邮箱验证码
     */
    public static final String TYPE_CODE_EMAIL = "email";

    /**
     * 场景编码
     * 注册
     */
    public static final String SCENE_CODE_REGISTER = "register";

    /**
     * 场景编码
     * 登录
     */
    public static final String SCENE_CODE_LOGIN = "login";

    /**
     * 场景编码
     * 找回密码
     */
    public static final String SCENE_CODE_FIND_PASSWORD = "findPassword";

    /**
     * 场景编码
     * 修改密码
     */
    public static final String SCENE_CODE_CHANGE_PASSWORD = "changePassword";

    /**
     * 场景编码
     * 绑定手机
     */
    public static final String SCENE_CODE_BIND_PHONE = "bindPhone";

    /**
     * 场景编码
     * 绑定邮箱
     */
    public static final String SCENE_CODE_BIND_EMAIL = "bindEmail";

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 验证码类型编码
     * 如：短信验证码、邮箱验证码
     */
    @Column(name = "t_type_code",length = 64)
    private String typeCode;

    /**
     * 验证码场景编码
     * 如：注册、登录、找回密码、修改密码、绑定手机、绑定邮箱
     */
    @Column(name = "t_scene_code",length = 64)
    private String sceneCode;

    /**
     * 发送to
     */
    @Column(name = "t_to",length = 64)
    private String to;

    /**
     * 值
     */
    @Column(name = "t_value",length = 64)
    private String value;

    /**
     * 有效期截至
     */
    @Column(name = "gmt_expire")
    private Date gmtExpire;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

}
