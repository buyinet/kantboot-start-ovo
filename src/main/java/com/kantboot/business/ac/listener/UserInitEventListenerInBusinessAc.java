package com.kantboot.business.ac.listener;

import com.alibaba.fastjson2.JSON;
import com.kantboot.business.ac.domain.entity.BusAcUserInviteScore;
import com.kantboot.business.ac.domain.entity.BusAcUserInviteScoreLog;
import com.kantboot.business.ac.repository.BusAcUserInviteScoreLogRepository;
import com.kantboot.business.ac.repository.BusAcUserInviteScoreRepository;
import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.system.user.domain.entity.SysUser;
import com.kantboot.system.user.domain.entity.SysUserRole;
import com.kantboot.system.user.event.UserInitEvent;
import com.kantboot.system.user.service.ISysBalanceService;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户初始化完成的事件监听器
 */
@Log4j2
@Component
public class UserInitEventListenerInBusinessAc {

    @Resource
    private ISysSettingService settingService;

    @Resource
    private ISysBalanceService balanceService;

    @Resource
    private BusAcUserInviteScoreRepository acUserInviteScoreRepository;

    @Resource
    private BusAcUserInviteScoreLogRepository acUserInviteScoreLogRepository;


    // 获取用户的ac角色
    private String getAcRole(SysUser user) {
        String roleCode = "";
        // 是否是vip
        Boolean isVip = false;
        // 是否是合伙人
        Boolean isPartner = false;
        // 是否是高级合伙人
        Boolean isSeniorPartner = false;
        List<SysUserRole> roleList = user.getRoleList();
        for (SysUserRole role : roleList) {
            if (role.getRoleCode().equals("vip")) {
                isVip = true;
            }
            if (role.getRoleCode().equals("partner")) {
                isPartner = true;
            }
            if (role.getRoleCode().equals("seniorPartner")) {
                isSeniorPartner = true;
            }
        }
        if (isSeniorPartner) {
            roleCode = "seniorPartner";
        } else if (isPartner) {
            roleCode = "partner";
        } else if (isVip) {
            roleCode = "vip";
        } else {
            roleCode = "user";
        }
        return roleCode;
    }

    @EventListener
    public void handleUserInitEvent(UserInitEvent event) {
        SysUser user = event.getUser();
        log.info("监听到新的用户:{}", JSON.toJSONString(user));
        // 判断用户是否有上级
        if(user.getInviteUser()==null) {
            return;
        }
        // 有上级
        log.info("用户:{}有上级", user.getId());
        // 获取上级的ac角色
        String roleCode = getAcRole(user.getInviteUser());
        log.info("用户:{}的上级的ac角色是:{}", user.getId(), roleCode);
        // 获取邀请用户可以获得的积分

        String value = settingService.getValue("acProject", "inviteUserGetScoreOfUser");
        if (roleCode.equals("vip")) {
            value = settingService.getValue("acProject", "inviteUserGetScoreOfVip");
        }
        if (roleCode.equals("partner")) {
            value = settingService.getValue("acProject", "inviteUserGetScoreOfPartner");
        }
        if (roleCode.equals("seniorPartner")) {
            value = settingService.getValue("acProject", "inviteUserGetScoreOfSeniorPartner");
        }
        log.info("用户:{}的上级的ac角色:{}可以获得的积分是:{}", user.getId(), roleCode, value);
        // 给上级添加积分
        Double score = balanceService.addBalance(user.getInviteUser().getId(), "score", Double.valueOf(value));
        log.info("用户:{}的上级的ac角色:{}添加后的积分是:{}", user.getId(), roleCode, JSON.toJSONString(score));

        // 记录日志
        BusAcUserInviteScoreLog busAcUserInviteScoreLog = new BusAcUserInviteScoreLog();
        busAcUserInviteScoreLog.setUserId(user.getInviteUser().getId());
        busAcUserInviteScoreLog.setInvitedUserId(user.getId());
        busAcUserInviteScoreLog.setScore(new BigDecimal(value));
        busAcUserInviteScoreLog.setAcRoleCode(roleCode);
        acUserInviteScoreLogRepository.save(busAcUserInviteScoreLog);

        BusAcUserInviteScore byUserId = acUserInviteScoreRepository.findByUserId(user.getInviteUser().getId());
        if (byUserId == null) {
            // 如果没有记录，就创建一条
            BusAcUserInviteScore busAcUserInviteScore = new BusAcUserInviteScore();
            busAcUserInviteScore.setUserId(user.getInviteUser().getId());
            busAcUserInviteScore.setScore(new BigDecimal(value));
            acUserInviteScoreRepository.save(busAcUserInviteScore);
            return;
        }

        // 增加邀请人的邀请积分
        acUserInviteScoreRepository.addScore(user.getInviteUser().getId(), Double.valueOf(value));

    }

}
