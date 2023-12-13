package com.kantboot.business.ac.service.impl;

import com.kantboot.business.ac.service.IBusAcCommonService;
import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.system.user.service.ISysBalanceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class BusAcCommonServiceImpl implements IBusAcCommonService {

    @Resource
    private ISysSettingService settingService;

    @Resource
    private ISysBalanceService balanceService;

    @Override
    public Map<String,String> getAcProjectSetting() {
        return settingService.getMapByGroupCode("acProject");
    }

    @Override
    public Double getSelfBalanceScoreToRmb() {
        // 一元人民币可以兑换多少积分
        String value = settingService.getValue("acProject", "moneyOfOneToScore");
        // 自己的积分余额
        Double score = balanceService.getSelf().get("score");
        BigDecimal bigDecimal = new BigDecimal(score);
        BigDecimal bigDecimal1 = new BigDecimal(value);
        return bigDecimal.divide(bigDecimal1).doubleValue();
    }

}
