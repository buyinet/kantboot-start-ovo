package com.kantboot.system.user.web.admin.controller;

import com.kantboot.system.user.domain.dto.SysOrgSearchDTO;
import com.kantboot.system.user.domain.entity.SysOrg;
import com.kantboot.system.user.service.ISysOrgService;
import com.kantboot.util.common.result.RestResult;
import com.kantboot.util.core.controller.BaseAdminController;
import com.kantboot.util.core.param.PageParam;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system-user-web/admin/org")
public class SysOrgControllerOfAdmin extends BaseAdminController<SysOrg,Long> {

    @Resource
    private ISysOrgService service;

    @PostMapping("/getBodyData")
    public RestResult getBodyData(@RequestBody PageParam<SysOrgSearchDTO> pageParam) {
        return RestResult.success(service.getBodyData(pageParam),"getSuccess","获取成功");
    }

    @RequestMapping("/getAll")
    public RestResult getAll() {
        return RestResult.success(service.getAll(),"getSuccess","获取成功");
    }


}
