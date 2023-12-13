package com.kantboot.business.dtu.service.impl;

import com.kantboot.business.dtu.domain.entity.BusDtu;
import com.kantboot.business.dtu.domain.entity.BusDtuStatus;
import com.kantboot.business.dtu.repository.BusDtuRepository;
import com.kantboot.business.dtu.repository.BusDtuStatusRepository;
import com.kantboot.business.dtu.service.IBusDtuService;
import com.kantboot.system.user.domain.entity.SysUser;
import com.kantboot.system.user.service.ISysUserService;
import com.kantboot.util.common.exception.BaseException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BusDtuService implements IBusDtuService {

    @Resource
    private BusDtuRepository repository;

    @Resource
    private BusDtuStatusRepository statusRepository;

    @Resource
    private ISysUserService userService;

    @Override
    public List<BusDtu> getSelfDtu() {
        SysUser self = userService.getSelf();
        if (self != null) {
            return repository.findByOrgId(self.getOrgId());
        }
        return new ArrayList<>();
    }

    @Override
    public BusDtu addDtu(BusDtu dtu) {
        BusDtu byImei = repository.findByImei(dtu.getImei());
        if (byImei != null) {
            throw BaseException.of("dtuImeiExist","IMEI已存在");
        }
        statusRepository.save(new BusDtuStatus().setImei(dtu.getImei()));
        return repository.save(dtu);
    }

    @Override
    public BusDtuStatus updateDtuStatus(BusDtuStatus status) {
        return statusRepository.save(status);
    }

    @Override
    public List<BusDtuStatus> updateDtuStatusBatch(List<BusDtuStatus> statusList) {
        return statusRepository.saveAll(statusList);
    }
}
