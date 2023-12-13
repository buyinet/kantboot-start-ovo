package com.kantboot.system.user.service;

import com.kantboot.system.user.domain.entity.SysBalance;

import java.util.List;
import java.util.Map;

public interface ISysBalanceService {

    /**
     * 获取所有的余额的
     */
    List<SysBalance> getAll();

    /**
     * 根据用户id获取所有余额
     */
    Map<String,Double> getByUserId(Long userId);

    /**
     * 获取用户本身的余额
     */
    Map<String,Double> getSelf();

    /**
     * 给用户增加余额
     */
    Double addBalance(Long userId, String balanceCode, Double number);


}
