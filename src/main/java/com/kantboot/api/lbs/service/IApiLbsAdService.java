package com.kantboot.api.lbs.service;

import com.kantboot.api.lbs.domain.vo.ApiLbsAdHasAdlvVO;
import com.kantboot.api.lbs.domain.vo.ApiLbsAdHasChildrenVO;

import java.util.List;

/**
 * 获取地理位置
 */
public interface IApiLbsAdService {

    /**
     * 获取所有
     */
    List<ApiLbsAdHasChildrenVO> getAllHasChildren();

    /**
     * 获取所有
     */
    List<ApiLbsAdHasAdlvVO> getAll();

    /**
     * 根据ip获取地理位置
     */
    ApiLbsAdHasAdlvVO getByIp(String ip);

    /**
     * 根据自身ip获取地理位置
     */
    ApiLbsAdHasAdlvVO getBySelfIp();

    /**
     * 根据经纬度获取地理位置
     */
    ApiLbsAdHasAdlvVO getByLngAndLat(String lng, String lat);

}
