package com.kantboot.system.user.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IDefaultAvatarService {

    /**
     * 访问
     */
    ResponseEntity<FileSystemResource> visit(String path);

    /**
     * 获取所有默认头像路径
     */
    List<String> getAll();

    /**
     * 获取男生默认头像路径
     */
    List<String> getMaleList();

    /**
     * 获取女生默认头像路径
     */
    List<String> getFemaleList();

}
