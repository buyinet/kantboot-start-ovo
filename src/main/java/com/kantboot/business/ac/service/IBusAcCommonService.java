package com.kantboot.business.ac.service;

import java.util.Map;

/**
 * ac项目的通用service
 */
public interface IBusAcCommonService {

    /**
     * 获取acProject的配置
     */
    Map<String,String> getAcProjectSetting();

    /**
     * 计算自己的积分余额可以兑换多少人民币
     */
    Double getSelfBalanceScoreToRmb();

}
