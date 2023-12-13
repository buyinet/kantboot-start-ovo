package com.kantboot.business.ac.service.impl;

import com.kantboot.business.ac.domain.entity.BusAcUserInviteScore;
import com.kantboot.business.ac.repository.BusAcUserInviteScoreRepository;
import com.kantboot.business.ac.service.IBusAcUserInviteScoreService;
import com.kantboot.system.user.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BusAcUserInviteScoreService implements IBusAcUserInviteScoreService {

    @Resource
    private BusAcUserInviteScoreRepository repository;

    @Resource
    private ISysUserService userService;

    @Override
    public BigDecimal getScoreByUserIdOfInvite(Long userId) {
        BusAcUserInviteScore byUserId = repository.findByUserId(userId);
        if (byUserId == null) {
            return BigDecimal.ZERO;
        }
        if (byUserId.getScore() == null) {
            return BigDecimal.ZERO;
        }
        return byUserId.getScore();
    }

    @Override
    public BigDecimal getSelfScoreOfInvite() {
        return getScoreByUserIdOfInvite(userService.getSelfId());
    }
}
