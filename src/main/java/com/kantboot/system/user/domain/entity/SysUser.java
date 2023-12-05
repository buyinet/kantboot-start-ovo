package com.kantboot.system.user.domain.entity;

import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户实体类
 * 为了防止后期做分库库表，所以不用manyToMany关联角色表
 * 在VO中，使用List<String> roleCodeList来关联角色表
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "sys_user")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class SysUser implements Serializable {

    /**
     * Sadomasochism属性
     * 参与: sado
     */
    public static final String SADOMASOCHISM_CODE_SADO = "sado";

    /**
     * Sadomasochism属性
     * 被参与: maso
     */
    public static final String SADOMASOCHISM_CODE_MASO = "maso";

    /**
     * Sadomasochism属性
     * 皆可：switch
     */
    public static final String SADOMASOCHISM_CODE_SWITCH = "switch";

    /**
     * Sadomasochism属性
     * dom
     */
    public static final String SADOMASOCHISM_CODE_DOM = "dom";

    /**
     * Sadomasochism属性
     * sub
     */
    public static final String SADOMASOCHISM_CODE_SUB = "sub";

    /**
     * Sadomasochism属性
     * 不参与: none
     */
    public static final String SADOMASOCHISM_CODE_NONE = "none";

    /**
     * Sadomasochism属性
     * 尚且未知：unknown
     */
    public static final String SADOMASOCHISM_CODE_UNKNOWN = "unknown";



    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 是否为临时用户，是没有绑定账号的用户，比如微信登录还没有绑定用户账号
     */
    @Column(name = "is_temporary")
    private Boolean isTemporary;

    /**
     * 是否已初始化完成
     */
    @Column(name = "is_init")
    private Boolean isInit;

    /**
     * 用户账号
     */
    @Column(name = "username", length = 64)
    private String username;

    /**
     * 手机号
     */
    @Column(name = "phone", length = 64)
    private String phone;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 性别编码
     * 男：male
     * 女：female
     * 未知：unknown
     * 保密：secret
     */
    @Column(name="gender_code")
    private String genderCode;

    /**
     * 密码
     * 此处非明文密码，而是加密后的密码
     * 在我的kantboot中，当然是用了我自己的KantbootPassword方式加密
     */
    @Column(name = "password")
    private String password;

    /**
     * 昵称
     */
    @Column(name = "nickname", length = 64)
    private String nickname;

    /**
     * 头像的文件id
     */
    private Long fileIdOfAvatar;

    /**
     * 用户自我介绍
     */
    @Column(name = "introduction", length = 1024)
    private String introduction;

    /**
     * 生日时间
     */
    @Column(name = "gmt_birthday")
    private Date gmtBirthday;

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

    @OneToMany
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private List<SysUserRole> roleList;

    /**
     * 属性
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<SysUserAttribute> attributeList;

    /**
     * 关联在线表
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    private SysUserOnline online;

    /**
     * 性取向
     */
    @Column(name = "sexual_orientation")
    private String sexualOrientation;

    /**
     * Sadomasochism属性
     */
    @Column(name = "sadomasochism_code")
    private String sadomasochismCode;


}
