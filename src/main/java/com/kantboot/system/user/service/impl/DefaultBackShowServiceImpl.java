package com.kantboot.system.user.service.impl;

import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.system.user.service.IDefaultBackShowService;
import jakarta.annotation.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 访问默认背景图片服务接口
 */
@Service
public class DefaultBackShowServiceImpl implements IDefaultBackShowService {

    @Resource
    private ISysSettingService settingService;

    @Override
    public ResponseEntity<FileSystemResource> visit(String path) {
        return null;
    }

    @Override
    public List<String> getAll() {
        String path = settingService.getValue("defaultImageStock", "backShowFolderPath");
        int pathLength = path.length();
        List<String> result = new ArrayList<>();

        File file = new File(path+"/sado");
        File[] files = file.listFiles();
        for (File file1 : files) {
            result.add(file1.getAbsolutePath().substring(pathLength).replaceAll("\\\\","/"));
        }
        File file2 = new File(path+"/maso");
        File[] files2 = file2.listFiles();
        for (File file1 : files2) {
            result.add(file1.getAbsolutePath().substring(pathLength).replaceAll("\\\\","/"));
        }
        File file3 = new File(path+"/switch");
        File[] files3 = file3.listFiles();
        for (File file1 : files3) {
            result.add(file1.getAbsolutePath().substring(pathLength).replaceAll("\\\\","/"));
        }
        File file4 = new File(path+"/dom");
        File[] files4 = file4.listFiles();
        for (File file1 : files4) {
            result.add(file1.getAbsolutePath().substring(pathLength).replaceAll("\\\\","/"));
        }
        File file5 = new File(path+"/sub");
        File[] files5 = file5.listFiles();
        for (File file1 : files5) {
            result.add(file1.getAbsolutePath().substring(pathLength).replaceAll("\\\\","/"));
        }
        File file6 = new File(path+"/none");
        File[] files6 = file6.listFiles();
        for (File file1 : files6) {
            result.add(file1.getAbsolutePath().substring(pathLength).replaceAll("\\\\","/"));
        }
        File file7 = new File(path+"/unknown");
        File[] files7 = file7.listFiles();
        for (File file1 : files7) {
            result.add(file1.getAbsolutePath().substring(pathLength).replaceAll("\\\\","/"));
        }
        return result;
    }
}
