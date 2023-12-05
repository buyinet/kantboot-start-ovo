package com.kantboot.functional.file.service;

import com.kantboot.functional.file.domain.entity.FunctionalFile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件管理的Service接口
 * 用于管理文件的上传、下载、删除等
 * @author 方某方
 */
public interface IFunctionalFileService {

    /**
     * 上传文件(需要指定文件组编码)
     * @param file 文件
     * @param groupCode 文件组编码
     * @param code 文件编码
     * @return 文件
     */
    FunctionalFile upload(MultipartFile file, String groupCode,String code);


    /**
     * 根据id查询文件
     * @param id 文件id
     * @return 文件
     */
    FunctionalFile getById(Long id);

    /**
     * 查看文件
     * @param id 文件id
     * @return 文件
     */
    ResponseEntity<FileSystemResource> visit(Long id);

    /**
     * 根据分组编码和编码访问文件
     */
    ResponseEntity<FileSystemResource> visitByGroupCodeAndCode(String groupCode, String code);

    /**
     * 获取某个分组下的文件
     */
    List<FunctionalFile> getListByGroupCode(String groupCode);


}
