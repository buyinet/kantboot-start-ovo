package com.kantboot.api.lbs.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LbsVO implements Serializable {

    /**
     * ip
     */
    private String ip;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 经度
     */
    private String lng;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 街道
     */
    private String street;

    /**
     * 位置编号
     */
    private String adcode;

}
