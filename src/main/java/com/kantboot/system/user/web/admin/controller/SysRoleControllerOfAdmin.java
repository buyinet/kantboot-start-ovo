package com.kantboot.system.user.web.admin.controller;

import com.kantboot.system.user.domain.entity.SysOrg;
import com.kantboot.system.user.service.ISysRoleService;
import com.kantboot.util.common.result.RestResult;
import com.kantboot.util.core.controller.BaseAdminController;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system-user-web/admin/role")
public class SysRoleControllerOfAdmin extends BaseAdminController<SysOrg,Long> {

    @Resource
    private ISysRoleService service;

    @RequestMapping("/getAll")
    public RestResult getAll() {
        return RestResult.success(service.getAll(),"getSuccess","获取成功");
    }
    

}
