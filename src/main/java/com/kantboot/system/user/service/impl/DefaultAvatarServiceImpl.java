package com.kantboot.system.user.service.impl;

import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.system.user.service.IDefaultAvatarService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DefaultAvatarServiceImpl implements IDefaultAvatarService {

    @Resource
    private HttpServletResponse response;

    @Resource
    private HttpServletRequest request;

    @Resource
    private ISysSettingService settingService;

    @Override
    public ResponseEntity<FileSystemResource> visit(String path) {
        String value = settingService.getValue("defaultImageStock", "avatarFolderPath");
        FileSystemResource resource = new FileSystemResource(value+"/"+path);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", CacheControl.maxAge(7, TimeUnit.HOURS).getHeaderValue());
        headers.add("Content-Disposition", "inline;filename=" + value);
        headers.add("Content-Type", "image/png");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @Override
    public List<String> getAll() {
        String path = settingService.getValue("defaultImageStock", "avatarFolderPath");
        int pathLength = path.length();
        List<String> result = new ArrayList<>();
        // 取male和female的头像路径
        File file = new File(path+"/male");
        File[] files = file.listFiles();
        for (File file1 : files) {
            result.add(file1.getAbsolutePath().substring(pathLength).replaceAll("\\\\","/"));
        }
        File file2 = new File(path+"/female");
        File[] files2 = file2.listFiles();
        for (File file1 : files2) {
            result.add(file1.getAbsolutePath().substring(pathLength).replaceAll("\\\\","/"));
        }
        return result;
    }

    @Override
    public List<String> getMaleList() {
        String path = settingService.getValue("defaultImageStock", "avatarFolderPath");
        int pathLength = path.length();
        List<String> result = new ArrayList<>();
        // 取male和female的头像路径
        File file = new File(path+"/male");
        File[] files = file.listFiles();
        for (File file1 : files) {
            result.add(file1.getAbsolutePath().substring(pathLength).replaceAll("\\\\","/"));
        }
        return result;
    }

    @Override
    public List<String> getFemaleList() {
        String path = settingService.getValue("defaultImageStock", "avatarFolderPath");
        int pathLength = path.length();
        List<String> result = new ArrayList<>();
        // 取male和female的头像路径
        File file = new File(path+"/female");
        File[] files = file.listFiles();
        for (File file1 : files) {
            result.add(file1.getAbsolutePath().substring(pathLength).replaceAll("\\\\","/"));
        }
        return result;
    }

}
