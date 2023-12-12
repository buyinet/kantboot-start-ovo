package com.kantboot.api.lbs.controller;

import com.kantboot.api.lbs.domain.vo.ApiLbsAdHasChildrenVO;
import com.kantboot.api.lbs.service.IApiLbsAdService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api-lbs/ad")
public class ApiLbsAdController {

    @Resource
    private IApiLbsAdService service;

    /**
     * 获取所有(包含子节点)
     */
    @RequestMapping("/getAllHasChildren")
    public RestResult<List<ApiLbsAdHasChildrenVO>> getAllHasChildren() {
        return RestResult.success(service.getAllHasChildren(), "getSuccess","获取成功");
    }

    /**
     * 获取所有
     */
    @RequestMapping("/getAll")
    public RestResult getAll() {
        return RestResult.success(service.getAll(), "getSuccess","获取成功");
    }

    /**
     * 根据ip获取地理位置
     */
    @RequestMapping("/getByIp")
    public RestResult getByIp(@RequestParam(value = "ip") String ip) {
        return RestResult.success(service.getByIp(ip), "getSuccess","获取成功");
    }

    /**
     * 根据自身ip获取地理位置
     */
    @RequestMapping("/getBySelfIp")
    public RestResult getBySelfIp() {
        return RestResult.success(service.getBySelfIp(), "getSuccess","获取成功");
    }

    /**
     * 根据经纬度获取地理位置
     */
    @RequestMapping("/getByLngAndLat")
    public RestResult getByLngAndLat(@RequestParam(value = "lng") String lng, @RequestParam(value = "lat") String lat) {
        return RestResult.success(service.getByLngAndLat(lng, lat), "getSuccess","获取成功");
    }


}
