package com.kantboot.business.ac.service;

import java.math.BigDecimal;

public interface IBusAcUserInviteScoreService {

    /**
     * 根据userId获取积分
     */
    BigDecimal getScoreByUserIdOfInvite(Long userId);

    /**
     * 获取自己的邀请积分
     */
    BigDecimal getSelfScoreOfInvite();

}
