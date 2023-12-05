package com.kantboot.system.user.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 访问默认背景图片服务接口
 */
public interface IDefaultBackShowService {
    // TODO 未完善

    /**
     * 访问
     */
    ResponseEntity<FileSystemResource> visit(String path);

    /**
     * 获取所有默认背景图片路径
     */
    List<String> getAll();

}
