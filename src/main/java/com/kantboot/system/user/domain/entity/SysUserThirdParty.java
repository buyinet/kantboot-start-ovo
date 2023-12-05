package com.kantboot.system.user.domain.entity;

import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "sys_user_third_party")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SysUserThirdParty {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 第三方平台编码
     * 如：github、wechat、qq等
     */
    @Column(name = "third_party_code")
    private String thirdPartyCode;

    /**
     * 识别标识的key
     */
    @Column(name = "t_key")
    private String key;

    /**
     * 采用的字段value
     */
    @Column(name = "t_value")
    private String value;

    /**
     * 创建时间
     */
    @CreatedBy
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

}
