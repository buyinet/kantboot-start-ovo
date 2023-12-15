package com.kantboot.business.dtu.controller;

import com.kantboot.business.dtu.domain.entity.BusDtu;
import com.kantboot.business.dtu.domain.entity.BusDtuStatus;
import com.kantboot.business.dtu.service.IBusDtuService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/business-dtu-web/dtu")
public class BusDtuController {

    @Resource
    private IBusDtuService service;

    @RequestMapping("/getSelfDtu")
    public RestResult<List<BusDtu>> getSelfDtu() {
        return RestResult.success(service.getSelfDtu(),"getSuccess","获取成功");
    }

    @RequestMapping("/addDtu")
    public RestResult<BusDtu> addDtu(
            @RequestBody
            BusDtu dtu) {
        return RestResult.success(service.addDtu(dtu),"addSuccess","添加成功");
    }

    @RequestMapping("/updateDtuStatus")
    public RestResult<BusDtuStatus> updateDtuStatus(
            @RequestBody BusDtuStatus entity) {
        return RestResult.success(service.updateDtuStatus(entity),"updateSuccess","更新成功");
    }

    @RequestMapping("/updateDtuStatusBatch")
    public RestResult<List<BusDtuStatus>> updateDtuStatusBatch(@RequestBody List<BusDtuStatus> entityList) {
        return RestResult.success(service.updateDtuStatusBatch(entityList),"updateSuccess","更新成功");
    }




}
