package com.kantboot.business.dtu.listener;

import com.kantboot.business.dtu.domain.entity.BusDtu;
import com.kantboot.business.dtu.event.BusDtuFlushEvent;
import com.kantboot.business.dtu.repository.BusDtuRepository;
import com.kantboot.socket.domain.dto.SocketPushMessageDTO;
import com.kantboot.socket.domain.entity.SocketPushMessagePayload;
import com.kantboot.socket.service.ISocketPushService;
import com.kantboot.system.user.domain.entity.SysUser;
import com.kantboot.system.user.service.ISysUserService;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class BusDtuFlushEventListener {

    @Resource
    private BusDtuRepository dtuRepository;

    @Resource
    private ISysUserService userService;

    @Resource
    private ISocketPushService socketPushService;

    @EventListener
    public void onApplicationEvent(BusDtuFlushEvent event) {
        log.info("DTU刷新事件:{}", event.getDtu());
        BusDtu dtu = event.getDtu();
        SocketPushMessageDTO message = new SocketPushMessageDTO();
        message.setTtl(0L);
        // 查询出是哪个组织的dtu
        Long orgId = dtu.getOrgId();
        log.info("这个DTU属于组织:{}", orgId);
        // 查询出这个组织的所有用户
        List<SysUser> byOrgId = userService.getListByOrgId(orgId);
//        log.info("给这些用户推送消息:{}", JSON.toJSONString(byOrgId));
        SocketPushMessagePayload payload = new SocketPushMessagePayload();
        payload.setEmit("  ");
        message.setPayload(payload);
        // 给这些用户推送消息
        for (SysUser user : byOrgId) {
            message.setUserId(user.getId());

            log.info("给用户{}推送消息:{}", user.getId(), message);
            socketPushService.pushOfUniPush(message);
        }
    }

}
