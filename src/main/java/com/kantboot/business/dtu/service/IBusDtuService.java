package com.kantboot.business.dtu.service;

import com.kantboot.business.dtu.domain.entity.BusDtu;
import com.kantboot.business.dtu.domain.entity.BusDtuStatus;

import java.util.List;

/**
 * DTU的业务接口
 */
public interface IBusDtuService {

    /**
     * 获取自己的DTU
     */
    List<BusDtu> getSelfDtu();

    /**
     * 初始化DTU
     */
    void initDtu(BusDtu dtu);

    /**
     * 添加DTU
     */
    BusDtu addDtu(BusDtu dtu);

    /**
     * 更新DTU状态
     */
    BusDtuStatus updateDtuStatus(BusDtuStatus status);

    /**
     * 批量更新DTU状态
     */
    List<BusDtuStatus> updateDtuStatusBatch(List<BusDtuStatus> statusList);

}
