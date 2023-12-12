package com.kantboot.api.lbs.domain.vo;

import com.kantboot.api.lbs.domain.entity.ApiLbsAd;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

@Table(name = "api_lbs_ad")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class ApiLbsAdHasAdlvVO implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * 行政区划代码
     */
    @Column(name = "adcode")
    private String adcode;

    /**
     * 行政区划级别
     */
    @Column(name = "adlv")
    private Integer adlv;

    /**
     * 行政区划级别1的行政区划代码
     */
    @Column(name = "adlv1_adcode")
    private String adlv1Adcode;

    /**
     * 行政区划级别2的行政区划代码
     */
    @Column(name = "adlv2_adcode")
    private String adlv2Adcode;

    /**
     * 行政区划级别3的行政区划代码
     */
    @Column(name = "adlv3_adcode")
    private String adlv3Adcode;

    /**
     * 行政区划名称
     */
    @Column(name = "t_name")
    private String name;

    /**
     * 上级行政区划代码
     */
    @Column(name = "parent_adcode")
    private String parentAdcode;


    /**
     * 关联1级行政区划代码
     */
    @OneToOne
    @JoinColumn(name = "adlv1_adcode", referencedColumnName = "adcode", insertable = false, updatable = false)
    private ApiLbsAd adlv1;

    /**
     * 关联2级行政区划代码
     */
    @OneToOne
    @JoinColumn(name = "adlv2_adcode", referencedColumnName = "adcode", insertable = false, updatable = false)
    private ApiLbsAd adlv2;

    /**
     * 关联3级行政区划代码
     */
    @OneToOne
    @JoinColumn(name = "adlv3_adcode", referencedColumnName = "adcode", insertable = false, updatable = false)
    private ApiLbsAd adlv3;

}
