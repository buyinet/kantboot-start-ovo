package com.kantboot.system.setting.web.admin.controller;

import com.kantboot.system.setting.domain.entity.SysSetting;
import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.util.common.result.RestResult;
import com.kantboot.util.core.controller.BaseAdminController;
import com.kantboot.util.core.param.PageParam;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system-setting-web/admin/setting")
public class SysSettingControllerOfAdmin extends BaseAdminController<SysSetting,Long> {

    @Resource
    private ISysSettingService service;

    @RequestMapping("/getBodyData")
    public RestResult getBodyData(@RequestBody PageParam<SysSetting> pageParam){
        return RestResult.success(service.getBodyData(pageParam),"getSuccess","获取成功");
    }

}
