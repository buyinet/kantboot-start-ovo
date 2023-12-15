package com.kantboot.business.dtu.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson2.JSON;
import com.kantboot.business.dtu.domain.entity.BusDtu;
import com.kantboot.business.dtu.domain.entity.BusDtuStatus;
import com.kantboot.business.dtu.event.BusDtuFlushEvent;
import com.kantboot.business.dtu.event.BusDtuStatusChangeEvent;
import com.kantboot.business.dtu.repository.BusDtuRepository;
import com.kantboot.business.dtu.repository.BusDtuStatusRepository;
import com.kantboot.business.dtu.service.IBusDtuService;
import com.kantboot.socket.service.ISocketPushService;
import com.kantboot.system.user.domain.entity.SysUser;
import com.kantboot.system.user.service.ISysUserService;
import com.kantboot.util.common.exception.BaseException;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class BusDtuServiceImpl implements IBusDtuService {

    private final ApplicationEventPublisher applicationEventPublisher;

    public BusDtuServiceImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Resource
    private BusDtuRepository repository;

    @Resource
    private BusDtuStatusRepository statusRepository;

    @Resource
    private ISysUserService userService;

    @Resource
    private ISocketPushService socketPushService;


    @Override
    public List<BusDtu> getSelfDtu() {
        SysUser self = userService.getSelf();
        if (self != null) {
            return repository.findByOrgId(self.getOrgId());
        }
        return new ArrayList<>();
    }

    @Override
    public void initDtu(BusDtu dtu) {
        BusDtu byImei = repository.findByImei(dtu.getImei());
        if (byImei != null) {
            throw BaseException.of("dtuImeiExist", "IMEI已存在");
        }
        BusDtuStatus busDtuStatus = new BusDtuStatus().setImei(dtu.getImei());
        if (byImei.getStatus() != null) {
            busDtuStatus.setId(byImei.getStatus().getId());
        }
        statusRepository.save(busDtuStatus);
        // 设置成未初始化
        dtu.setIsInit(false);
        repository.save(dtu);
    }

    @Override
    public BusDtu addDtu(BusDtu dtu) {
        SysUser self = userService.getSelf();
        BusDtu byImei = repository.findByImei(dtu.getImei());
        if (byImei != null) {
            dtu.setId(byImei.getId());
        }
        if (byImei != null && byImei.getIsInit()) {
            throw BaseException.of("dtuImeiExist", "IMEI已存在");
        }
        BusDtuStatus busDtuStatus = new BusDtuStatus().setImei(dtu.getImei());
        // 设置安装人
        dtu.setUserIdOfInstall(self.getId());
        // 设置机构
        dtu.setOrgId(self.getOrgId());
        dtu.setIsInit(true);
        if (byImei.getStatus() != null) {
            busDtuStatus.setId(byImei.getStatus().getId());
        }
        BusDtuFlushEvent event = new BusDtuFlushEvent(this, dtu);
        applicationEventPublisher.publishEvent(event);

        statusRepository.save(busDtuStatus);
        return repository.save(dtu);
    }

    @Override
    public BusDtuStatus updateDtuStatus(BusDtuStatus status) {
        BusDtuStatus byImei = statusRepository.findByImei(status.getImei());
        if (byImei == null) {
            initDtu(new BusDtu().setImei(status.getImei()));
            throw BaseException.of("dtuImeiNotExist", "IMEI不存在");
        }
        String jsonString = JSON.toJSONString(status);
        String md5 = MD5.create().digestHex(jsonString);
        if (md5.equals(byImei.getMd5())) {
            return byImei;
        }
        status.setMd5(md5);
        status.setId(byImei.getId());
        BusDtuStatusChangeEvent event = new BusDtuStatusChangeEvent(this, status);
        applicationEventPublisher.publishEvent(event);

        return statusRepository.save(status);
    }

    @Override
    public List<BusDtuStatus> updateDtuStatusBatch(List<BusDtuStatus> statusList) {
        List<BusDtuStatus> result = new ArrayList<>();
        for (BusDtuStatus status : statusList) {
            result.add(updateDtuStatus(status));
        }
        return result;
    }

}
