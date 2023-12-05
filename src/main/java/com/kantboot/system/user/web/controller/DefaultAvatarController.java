package com.kantboot.system.user.web.controller;

import com.kantboot.system.user.service.IDefaultAvatarService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 默认头像控制器
 * 1. 获取默认头像列表
 */
@RestController
@RequestMapping("/system-user-web/defaultAvatar")
public class DefaultAvatarController {

    @Resource
    private IDefaultAvatarService service;

    /**
     * 访问
     */
    @RequestMapping("/visit")
    public ResponseEntity<FileSystemResource> visit(@RequestParam("path") String path) {
       return service.visit(path);
    }

    /**
     * 获取默认头像列表
     * @return 默认头像列表
     */
    @RequestMapping("/getAll")
    public RestResult getAll() {
        return RestResult.success(service.getAll(),"getSuccess","获取成功");
    }

    /**
     * 获取男生默认头像列表
     * @return 默认头像列表
     */
    @RequestMapping("/getMaleList")
    public RestResult getMaleList() {
        return RestResult.success(service.getMaleList(),"getSuccess","获取成功");
    }

    /**
     * 获取女生默认头像列表
     * @return 默认头像列表
     */
    @RequestMapping("/getFemaleList")
    public RestResult getFemaleList() {
        return RestResult.success(service.getFemaleList(),"getSuccess","获取成功");
    }


}
