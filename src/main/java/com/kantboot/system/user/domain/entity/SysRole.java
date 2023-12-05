package com.kantboot.system.user.domain.entity;

import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色实体类
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "sys_role")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SysRole implements Serializable {

    private static final long serialVersionUID = -7497041000507568149L;

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 角色编码
     * 用于区分不同的角色
     * 也对应着字典中的角色编码
     */
    @Column(name = "t_code", length = 64)
    private String code;

    /**
     * 角色名称
     */
    @Column(name = "t_name")
    private String name;

    /**
     * 是否可见
     */
    @Column(name = "visible")
    private Boolean visible;

    /**
     * 角色描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 角色优先级
     */
    @Column(name = "priority")
    private Integer priority;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 最后一次修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

}
