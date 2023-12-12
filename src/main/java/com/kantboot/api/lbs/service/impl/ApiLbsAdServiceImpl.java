package com.kantboot.api.lbs.service.impl;

import com.kantboot.api.lbs.domain.entity.ApiLbsAd;
import com.kantboot.api.lbs.domain.vo.ApiLbsAdHasAdlvVO;
import com.kantboot.api.lbs.domain.vo.ApiLbsAdHasChildrenVO;
import com.kantboot.api.lbs.domain.vo.LbsVO;
import com.kantboot.api.lbs.repository.ApiLbsAdHasAdlvVORepository;
import com.kantboot.api.lbs.repository.ApiLbsAdHasChildrenVORepository;
import com.kantboot.api.lbs.repository.ApiLbsAdRepository;
import com.kantboot.api.lbs.service.IApiLbsAdService;
import com.kantboot.api.lbs.util.TencentLbsUtil;
import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.util.common.http.HttpRequestHeaderUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 获取地理位置
 */
@Service
public class ApiLbsAdServiceImpl implements IApiLbsAdService {

    @Resource
    private ApiLbsAdHasAdlvVORepository repository;

    @Resource
    private ApiLbsAdHasChildrenVORepository hasChildrenVORepository;

    @Resource
    private ISysSettingService settingService;

    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil;

    /**
     * 获取所有
     */
    @Override
    public List<ApiLbsAdHasChildrenVO> getAllHasChildren() {
        return hasChildrenVORepository.findAll();
    }

    @Override
    public List<ApiLbsAdHasAdlvVO> getAll() {
        return repository.findAll();
    }

    @Override
    public ApiLbsAdHasAdlvVO getByIp(String ip) {
        String tencentMapApiKey = settingService.getValue("lbs", "tencentMapApiKey");
        LbsVO lbsVO = TencentLbsUtil.byIp(ip, tencentMapApiKey);
        return repository.findByAdcode(lbsVO.getAdcode());
    }

    @Override
    public ApiLbsAdHasAdlvVO getBySelfIp() {
        return getByIp(httpRequestHeaderUtil.getIp());
    }


    @Override
    public ApiLbsAdHasAdlvVO getByLngAndLat(String lng, String lat) {
        String key = settingService.getValue("lbs", "tencentMapApiKey");
        LbsVO lbsVO = TencentLbsUtil.byLngAndLat(lng, lat, key);
        return repository.findByAdcode(lbsVO.getAdcode());
    }
}
