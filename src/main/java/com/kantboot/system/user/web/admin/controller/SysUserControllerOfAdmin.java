package com.kantboot.system.user.web.admin.controller;

import com.kantboot.system.user.domain.dto.SysUserSaveDTOOfDtu;
import com.kantboot.system.user.domain.dto.SysUserSearchDTO;
import com.kantboot.system.user.domain.entity.SysUser;
import com.kantboot.system.user.service.ISysUserService;
import com.kantboot.util.common.result.RestResult;
import com.kantboot.util.core.controller.BaseAdminController;
import com.kantboot.util.core.param.PageParam;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/system-user-web/admin/user")
public class SysUserControllerOfAdmin extends BaseAdminController<SysUser,Long> {

    @Resource
    private ISysUserService service;

    @RequestMapping("/getBodyData")
    public RestResult<Object> getBodyData(@RequestBody PageParam<SysUserSearchDTO> pageParam){
        return RestResult.success(service.getBodyData(pageParam),"getSuccess","获取成功");
    }

    /**
     * 给用户添加角色
     * @param userId 用户id
     * @param roleCodes 角色code列表
     */
    @PostMapping("/addRole")
    public RestResult<Object> addRole(
            @RequestParam("userId") Long userId,
            @RequestParam("roleCodes") List<String> roleCodes) {
        service.addRole(userId,roleCodes);
        return RestResult.success(null,"addSuccess","添加成功");
    }

    /**
     * 给用户删除角色
     */
    @PostMapping("/removeRole")
    public RestResult<Object> removeRole(
            @RequestParam("userId") Long userId,
            @RequestParam("roleCodes") List<String> roleCodes) {
        service.removeRole(userId,roleCodes);
        return RestResult.success(null,"deleteSuccess","删除成功");
    }

    /**
     * 给用户设置角色
     * @param userId 用户id
     * @param roleCodes 角色code列表
     */
    @PostMapping("/setRole")
    public RestResult setRole(
            @RequestParam("userId") Long userId,
            @RequestParam("roleCodes") List<String> roleCodes) {
        service.setRole(userId,roleCodes);
        return RestResult.success(null,"setSuccess","设置成功");
    }

    /**
     * saveOfDtu
     */
    @PostMapping("/saveOfDtu")
    public RestResult saveOfDtu(@RequestBody SysUserSaveDTOOfDtu user) {
        return RestResult.success(service.saveOfDtu(user),"saveSuccess","保存成功");
    }

}
