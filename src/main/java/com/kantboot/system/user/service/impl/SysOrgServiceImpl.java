package com.kantboot.system.user.service.impl;

import com.kantboot.system.user.dao.repository.SysOrgRepository;
import com.kantboot.system.user.domain.dto.SysOrgSearchDTO;
import com.kantboot.system.user.domain.entity.SysOrg;
import com.kantboot.system.user.service.ISysOrgService;
import com.kantboot.util.core.param.PageParam;
import com.kantboot.util.core.result.PageResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 组织机构服务实现类
 */
@Service
public class SysOrgServiceImpl implements ISysOrgService {

    @Resource
    private SysOrgRepository orgRepository;

    @Override
    public PageResult getBodyData(PageParam<SysOrgSearchDTO> pageParam) {
        return PageResult.of(orgRepository.getBodyData(pageParam.getData(),pageParam.getPageable()));
    }

    @Override
    public List<SysOrg> getAll() {
        return orgRepository.findAll();
    }
}
