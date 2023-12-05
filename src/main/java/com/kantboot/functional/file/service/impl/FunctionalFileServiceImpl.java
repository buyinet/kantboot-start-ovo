package com.kantboot.functional.file.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.functional.file.dao.repository.FunctionalFileRepository;
import com.kantboot.functional.file.domain.entity.FunctionalFile;
import com.kantboot.functional.file.domain.entity.FunctionalFileGroup;
import com.kantboot.functional.file.service.IFunctionalFileGroupService;
import com.kantboot.functional.file.service.IFunctionalFileService;
import com.kantboot.functional.file.util.FileUtil;
import com.kantboot.system.user.service.ISysTokenService;
import com.kantboot.util.common.exception.BaseException;
import com.kantboot.util.common.http.HttpRequestHeaderUtil;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 文件管理的Service接口实现类
 * 用于管理文件的上传、下载、删除等
 *
 * @author 方某某
 */
@Slf4j
@Service
public class FunctionalFileServiceImpl implements IFunctionalFileService {

    @Resource
    private FunctionalFileRepository repository;

    @Resource
    private IFunctionalFileGroupService fileGroupService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil;

    @Resource
    private ISysTokenService sysTokenService;


    /**
     * 添加进redis中
     */
    private void addRedis(FunctionalFile functionalFile) {
        String redisKey = "fileId:" + functionalFile.getId() + ":SysFile";
        redisUtil.setEx(redisKey, JSON.toJSONString(functionalFile), 7, TimeUnit.DAYS);
    }

    /**
     * 从redis中获取
     *
     * @param id 文件ID
     * @return 文件信息
     */
    private FunctionalFile getByRedis(Long id) {
        String redisKey = "fileId:" + id + ":SysFile";
        String s = redisUtil.get(redisKey);
        if (s != null) {
            return JSONObject.parseObject(s, FunctionalFile.class);
        }
        return null;
    }

    /**
     * 上传文件
     *
     * @param file           文件
     * @param fileUploadPath 文件上传路径
     * @return 文件信息
     */
    @SneakyThrows
    private FunctionalFile frontUploadFile(MultipartFile file, String fileUploadPath, String groupCode,String code) {
        Long userId =  null;

        try{
            userId = sysTokenService.getUserIdByToken(httpRequestHeaderUtil.getToken());
        }catch (Exception e){
            log.error("获取用户ID失败: {}", e.getMessage());
        }


        // 生成文件UUID
        String uuid = IdUtil.simpleUUID();
        // 获取文件MD5
        String md5 = DigestUtils.md5DigestAsHex(file.getBytes());
        // 获取上传时文件名
        String originalName = file.getOriginalFilename();

        // 如果文件后缀是m4a，则修改为mp3
        if (originalName != null && originalName.endsWith(".m4a")) {
            originalName = originalName.replace(".m4a", ".mp3");
        }

        // 检查文件是否已存在
        FunctionalFile byMd5 = repository.findFirstByMd5AndGroupCode(md5, groupCode);

        // 如果文件已存在
        if (byMd5 != null) {
            // 返回已存在的文件信息
            FunctionalFile save = repository.save(new FunctionalFile()
                    .setUserIdOfUpload(userId)
                            .setIpOfUpload(httpRequestHeaderUtil.getIp())
                    .setMd5(md5)
                    .setGroupCode(groupCode)
                    .setOriginalName(originalName)
                    .setName(byMd5.getName())
                    .setSize(byMd5.getSize())
                    .setContentType(byMd5.getContentType())
                    .setType(byMd5.getType())
                    .setContentType(byMd5.getContentType())
                    .setCode(code)
            );
            return save;
        }


        File fileUploadPathM = new File(fileUploadPath);
        // 如果文件夹不存在则创建
        if (!fileUploadPathM.exists()) {
            boolean mkdirs = fileUploadPathM.mkdirs();
            if (!mkdirs) {
                throw new Exception("文件夹创建失败");
            }
        }

        log.info("文件上传：{}", originalName);
        // assert关键字用于断言，如果表达式为false，则抛出AssertionError
        assert originalName != null;
        String suffix = originalName.substring(originalName.lastIndexOf(".") + 1);
        String path = uuid + "." + suffix;
        String contentType = FileUtil.getContentType(suffix);

        try {
            file.transferTo(new File(fileUploadPath + "/" + path));
        } catch (Exception e) {
            e.printStackTrace();
        }

        FunctionalFile save = repository.save(new FunctionalFile()
                .setUserIdOfUpload(userId)
                        .setIpOfUpload(httpRequestHeaderUtil.getIp())
                .setGroupCode(groupCode)
                .setMd5(md5)
                .setOriginalName(originalName)
                .setName(path)
                .setSize(file.getSize())
                .setContentType(FileUtil.getContentType(path))
                .setType(suffix)
                .setContentType(contentType)
                .setCode(code)
        );

        log.info("文件上传成功：{}", JSON.toJSONString(save));
        return save;
    }

    @Override
    public FunctionalFile upload(MultipartFile file, String groupCode,String code){
        FunctionalFileGroup byCode = fileGroupService.getByCode(groupCode);
        if (byCode == null) {
            throw BaseException.of("groupCodeNotExist", "文件组不存在");
        }
        // 获取文件上传路径
        String fileUploadPath = byCode.getPath() + "/";
        return frontUploadFile(file, fileUploadPath, groupCode,code);
    }


    @Override
    public FunctionalFile getById(Long id) {
        String redisKey = "fileId:" + id + ":FunctionalFile";
        String s = redisUtil.get(redisKey);
        if (s != null) {
            return JSONObject.parseObject(s, FunctionalFile.class);
        }
        FunctionalFile file = repository.findById(id).get();
        if (file != null) {
            addRedis(file);
            return file;
        }
        return file;
    }

    @Override
    public ResponseEntity<FileSystemResource> visit(Long id) {
        FunctionalFile byId = getById(id);
        String path = byId.getGroup().getPath()+"/"+byId.getName();
        FileSystemResource resource = new FileSystemResource(path);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", CacheControl.maxAge(7, TimeUnit.HOURS).getHeaderValue());
        headers.add("Content-Disposition", "inline;filename=" + byId.getName());
        headers.add("Content-Type", byId.getContentType());
        headers.add("Content-Length", String.valueOf(byId.getSize()));


        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @Override
    public ResponseEntity<FileSystemResource> visitByGroupCodeAndCode(String groupCode, String code) {
        FunctionalFile byGroupCodeAndCode = repository.findByGroupCodeAndCode(groupCode, code);
        String path = byGroupCodeAndCode.getGroup().getPath()+"/"+byGroupCodeAndCode.getName();
        FileSystemResource resource = new FileSystemResource(path);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", CacheControl.maxAge(7, TimeUnit.HOURS).getHeaderValue());
        headers.add("Content-Disposition", "inline;filename=" + byGroupCodeAndCode.getName());
        headers.add("Content-Type", byGroupCodeAndCode.getContentType());
        headers.add("Content-Length", String.valueOf(byGroupCodeAndCode.getSize()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @Override
    public List<FunctionalFile> getListByGroupCode(String groupCode) {
        return repository.findByGroupCode(groupCode);
    }
}
