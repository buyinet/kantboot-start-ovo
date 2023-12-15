package com.kantboot.business.dtu.domain.entity;


import com.kantboot.system.user.domain.entity.SysOrg;
import com.kantboot.system.user.domain.entity.SysUser;
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
@Table(name = "bus_dtu")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class BusDtu {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 主键
     */
    @Column(name = "imei",length = 64)
    private String imei;

    /**
     * 设备名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 设备备注
     */
    @Column(name = "t_remark")
    private String remark;


    /**
     * 地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 详细地址
     */
    @Column(name = "detail_address")
    private String detailAddress;

    /**
     * 经度
     */
    @Column(name = "longitude")
    private Double longitude;

    /**
     * 纬度
     */
    @Column(name = "latitude")
    private Double latitude;

    /**
     * 图片的文件id
     */
    @Column(name = "file_id_of_image")
    private String fileIdOfImage;

    /**
     * 安装的用户id
     */
    @Column(name = "user_id_of_install")
    private Long userIdOfInstall;

    /**
     * 安装的用户
     */
    @OneToOne
    @JoinColumn(name = "user_id_of_install",referencedColumnName = "id",insertable = false,updatable = false)
    private SysUser userOfInstall;



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

    /**
     * 所在组织id
     */
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 是否初始化
     */
    @Column(name = "is_init")
    private Boolean isInit;

    @OneToOne
    @JoinColumn(name = "org_id",referencedColumnName = "id",insertable = false,updatable = false)
    private SysOrg org;

    /**
     * 设备状态
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "imei",referencedColumnName = "imei",insertable = false,updatable = false)
    private BusDtuStatus status;

}
