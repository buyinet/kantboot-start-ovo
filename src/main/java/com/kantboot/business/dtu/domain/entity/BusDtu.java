package com.kantboot.business.dtu.domain.entity;


import com.kantboot.system.user.domain.entity.SysOrg;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
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
     * 经度
     */
    @Column(name = "longitude")
    private String longitude;

    /**
     * 纬度
     */
    @Column(name = "latitude")
    private String latitude;

    /**
     * 图片的文件id
     */
    @Column(name = "file_id_of_image")
    private String fileIdOfImage;


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

    @OneToOne
    @JoinColumn(name = "org_id",referencedColumnName = "id",insertable = false,updatable = false)
    private SysOrg org;

    /**
     * 设备状态
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "imei",referencedColumnName = "imei")
    private BusDtuStatus status;

}
