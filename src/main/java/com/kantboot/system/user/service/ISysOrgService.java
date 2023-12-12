package com.kantboot.system.user.service;

import com.kantboot.system.user.domain.dto.SysOrgSearchDTO;
import com.kantboot.system.user.domain.entity.SysOrg;
import com.kantboot.util.core.param.PageParam;
import com.kantboot.util.core.result.PageResult;

import java.util.List;

public interface ISysOrgService {

    PageResult getBodyData(PageParam<SysOrgSearchDTO> pageParam);

    List<SysOrg> getAll();

}
