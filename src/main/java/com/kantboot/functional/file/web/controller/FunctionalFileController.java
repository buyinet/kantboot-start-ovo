package com.kantboot.functional.file.web.controller;

import com.kantboot.functional.file.domain.entity.FunctionalFile;
import com.kantboot.functional.file.service.IFunctionalFileService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件
 * 用于管理文件的上传、下载、删除等
 */
@RestController
@RequestMapping("/functional-file/file")
public class FunctionalFileController {

    @Resource
    private IFunctionalFileService fileService;

    /**
     * 上传文件
     * @param file 文件
     * @param groupCode 文件组编码
     * @param code 文件编码
     * @return 文件
     */
    @RequestMapping("/upload")
    public RestResult<FunctionalFile> upload(MultipartFile file, String groupCode,String code) {
        return RestResult.success(fileService.upload(file, groupCode,code), "uploadSuccess","上传成功");
    }

    /**
     * 访问文件
     * @param id 文件id
     * @return 文件
     */
    @RequestMapping("/visit/{id}")
    public ResponseEntity<FileSystemResource> visit(
            @PathVariable Long id){
        return fileService.visit(id);
    }

    /**
     * 根据分组编码和编码访问文件
     * @param groupCode 分组编码
     * @param code 编码
     * @return 文件
     */
    @RequestMapping("/visitByGroupCodeAndCode/{groupCode}/{code}")
    public ResponseEntity<FileSystemResource> visitByGroupCodeAndCode(
            @PathVariable String groupCode,
            @PathVariable String code){
        return fileService.visitByGroupCodeAndCode(groupCode, code);
    }

    /**
     * 根据分组编码获取文件集合
     */
    @RequestMapping("/getListByGroupCode")
    public RestResult<Object> getListByGroupCode(@RequestParam("groupCode") String groupCode){
        return RestResult.success(fileService.getListByGroupCode(groupCode),"getSuccess","获取成功");
    }


}
