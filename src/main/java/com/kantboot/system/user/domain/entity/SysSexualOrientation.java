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

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "sys_sexual_orientation")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class SysSexualOrientation {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 性取向名称
     */
    @Column(name = "t_name", length = 64)
    private String name;

    /**
     * 性取向编码
     */
    @Column(name = "t_code", length = 64)
    private String code;

    /**
     * 性取向描述
     */
    @Column(name = "t_description", length = 64)
    private String description;

    /**
     * 性取向排序优先级
     */
    @Column(name = "t_priority")
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










