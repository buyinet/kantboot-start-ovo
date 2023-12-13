package com.kantboot.system.user.service.impl;

import com.kantboot.system.user.dao.repository.SysBalanceRepository;
import com.kantboot.system.user.dao.repository.SysUserBalanceRepository;
import com.kantboot.system.user.domain.entity.SysBalance;
import com.kantboot.system.user.domain.entity.SysUserBalance;
import com.kantboot.system.user.service.ISysBalanceService;
import com.kantboot.system.user.service.ISysTokenService;
import com.kantboot.util.common.exception.BaseException;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysBalanceServiceImpl implements ISysBalanceService {

    @Resource
    private SysBalanceRepository repository;

    @Resource
    private SysUserBalanceRepository userBalanceRepository;

    @Resource
    private ISysTokenService tokenService;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<SysBalance> getAll() {
        return repository.findAll();
    }

    @Override
    public HashMap<String,Double> getByUserId(Long userId) {
        List<SysBalance> all = getAll();
        HashMap<String,Double> map = new HashMap<>();
        for (SysBalance sysBalance : all) {
            map.put(sysBalance.getCode(), 0d);
        }
        List<SysUserBalance> byUserId = userBalanceRepository.findByUserId(userId);
        for (SysUserBalance sysUserBalance : byUserId) {
            map.put(sysUserBalance.getBalanceCode(), sysUserBalance.getNumber());
        }
        return map;
    }

    @Override
    public Map<String, Double> getSelf() {
        String selfToken = tokenService.getSelfToken();
        Long userId = tokenService.getUserIdByToken(selfToken);
        return getByUserId(userId);
    }

    @Override
    public Double addBalance(Long userId, String balanceCode, Double number) {
        String lockKey = "updateBalanceLock:code:" + balanceCode + ":userId:" + userId;
        if (redisUtil.lock(lockKey)) {
            throw BaseException.of("updateBalanceLock","更新余额失败，正在更新中，请稍后再试");
        }

        SysUserBalance save=null;
        // 获取用户余额
        SysUserBalance byUserIdAndCode = userBalanceRepository.findByUserIdAndBalanceCode(userId, balanceCode);
        if (byUserIdAndCode == null) {
            SysUserBalance sysUserBalance = new SysUserBalance();
            sysUserBalance.setUserId(userId);
            sysUserBalance.setBalanceCode(balanceCode);
            sysUserBalance.setNumber(number);
            save=userBalanceRepository.save(sysUserBalance);
        } else {
            BigDecimal bigDecimal = new BigDecimal(byUserIdAndCode.getNumber());
            BigDecimal bigDecimal1 = new BigDecimal(number);
            byUserIdAndCode.setNumber(bigDecimal.add(bigDecimal1).doubleValue());
            save=userBalanceRepository.save(byUserIdAndCode);
        }

        redisUtil.unlock(lockKey);
        return save.getNumber();
    }
}
