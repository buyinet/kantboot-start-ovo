package com.kantboot.system.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.kantboot.api.varcode.domain.entity.ApiVarCode;
import com.kantboot.api.varcode.service.IApiVarCodeService;
import com.kantboot.system.setting.service.ISysSettingService;
import com.kantboot.system.user.dao.repository.*;
import com.kantboot.system.user.domain.dto.SysUserInitInfoDTO;
import com.kantboot.system.user.domain.dto.SysUserSearchDTO;
import com.kantboot.system.user.domain.entity.*;
import com.kantboot.system.user.domain.vo.LoginVO;
import com.kantboot.system.user.service.ISysRoleService;
import com.kantboot.system.user.service.ISysTokenService;
import com.kantboot.system.user.service.ISysUserService;
import com.kantboot.util.common.exception.BaseException;
import com.kantboot.util.common.http.HttpRequestHeaderUtil;
import com.kantboot.util.common.password.KantbootPassword;
import com.kantboot.util.core.param.PageParam;
import com.kantboot.util.core.redis.RedisUtil;
import com.kantboot.util.core.result.PageResult;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 *
 * @author 方某方
 */
@Slf4j
@Service
public class SysUserServiceImpl implements ISysUserService {

    /**
     * redis前缀
     */
    private static final String REDIS_KEY_PREFIX = "sysUser";

    @Resource
    private SysUserRepository repository;

    @Resource
    private SysUserRoleRepository userRoleRepository;

    @Resource
    private ISysSettingService settingService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ISysRoleService roleService;

    @Resource
    private ISysTokenService tokenService;

    @Resource
    private SysUserOnlineRepository onlineRepository;

    @Resource
    private HttpRequestHeaderUtil httpRequestHeaderUtil;

    @Resource
    private SysPermissionRoleRepository permissionRoleRepository;

    @Resource
    private IApiVarCodeService apiVarCodeService;

    @Resource
    private SysUserThirdPartyRepository userThirdPartyRepository;

    @Override
    public String getDefaultRoleCode() {
        return settingService.getValue("user", "defaultRoleCode");
    }

    public Map<String, Object> entityToMap(SysUser entity) {
        // 开始时间
        long startTime = System.currentTimeMillis();
        if(entity.getOnline()==null){
            entity.setOnline(
                    new SysUserOnline()
                            .setOnline(false)
                            .setUserId(entity.getId())
                            .setOnlineStatusCode(SysUserOnline.ONLINE_STATUS_CODE_OFFLINE)
            );
        }

        entity.setPassword(null);
        Map<String, Object> result = BeanUtil.beanToMap(entity);

        Map<String, Object> attributeMap = new HashMap<>();
        List<SysUserAttribute> attributes = entity.getAttributeList();
        if (attributes == null) {
            attributes = new ArrayList<>();
        }

        for (SysUserAttribute attribute : attributes) {
            List<String> detailValues = attribute.getDetailList().stream()
                    .map(SysUserAttributeDetail::getValue)
                    .collect(Collectors.toList());
            attributeMap.put(attribute.getCode(), detailValues.isEmpty() ? attribute.getValue() : detailValues);
        }
        result.put("attr", attributeMap);

        // 移除属性列表
        entity.setAttributeList(null);

        List<String> roleCodes = new ArrayList<>();
        List<SysUserRole> roleList = entity.getRoleList();
        if (roleList == null) {
            roleList = new ArrayList<>();
        }


        roleList.forEach(role -> {
            roleCodes.add(role.getRoleCode());
            // 设置角色状态
            result.put("isRole" + StrUtil.upperFirst(role.getRoleCode()), true);
        });
        result.put("roleCodes", roleCodes);

        try{
            result.put("gmtCreate", entity.getGmtCreate().getTime());
            result.put("gmtModified", entity.getGmtModified().getTime());
        }catch (Exception e){
            result.put("gmtCreate", null);
            result.put("gmtModified", null);
        }
        // 结束时间
        long endTime = System.currentTimeMillis();
        log.info("entityToMap耗时：{}ms", endTime - startTime);
        return result;
    }

    private Map<String, Object> hideMap(SysUser vo) {
        Map<String, Object> map = entityToMap(vo);

        map.remove("username");
        List<SysUserRole> roleList = vo.getRoleList();
        List<SysUserRole> newRoleList = new ArrayList<>();
        for (SysUserRole role : roleList) {
            if(role.getVisible()!=null&&!role.getVisible()){
                newRoleList.add(role);
            }
        }
        map.remove("roleList");
        map.put("roleCodes", newRoleList.stream().map(SysUserRole::getRoleCode).collect(Collectors.toList()));

        return map;
    }


    @Override
    public Map<String, Object> getById(Long id) {
        SysUser sysUser = repository.findById(id).orElse(new SysUser());
        return entityToMap(sysUser);
    }

    @Override
    public Map<String, Object> getSelfById(Long id) {
        Map<String, Object> result = getById(id);
        List<String> roleCodes = (List<String>) result.get("roleCodes");
        List<SysPermissionRole> byRoleCodeIn = permissionRoleRepository.getByRoleCodeIn(roleCodes);
        List<String> permissionCodes = new ArrayList<>();
        for (SysPermissionRole sysPermissionRole : byRoleCodeIn) {
            permissionCodes.add(sysPermissionRole.getPermissionCode());
        }
        result.put("permissionCodes", permissionCodes);
        return result;
    }

    @Override
    public Map<String, Object> getSelf() {
        String selfToken = tokenService.getSelfToken();
        Long userIdByToken = tokenService.getUserIdByToken(selfToken);
        Map<String, Object> result = getById(userIdByToken);
        List<String> roleCodes = (List<String>) result.get("roleCodes");
        List<SysPermissionRole> byRoleCodeIn = permissionRoleRepository.getByRoleCodeIn(roleCodes);
        List<String> permissionCodes = new ArrayList<>();
        for (SysPermissionRole sysPermissionRole : byRoleCodeIn) {
            permissionCodes.add(sysPermissionRole.getPermissionCode());
        }
        result.put("permissionCodes", permissionCodes);
        return result;
    }

    @Override
    public Long getSelfId() {
        return tokenService.getUserIdByToken(tokenService.getSelfToken());
    }

    @Override
    public LoginVO login(String username, String password) {
        // 去除空格
        username = username.trim();
        password = password.trim();

        // 判断用户名是否存在
        SysUser byUsername = repository.findByUsername(username);
        if (byUsername == null) {
            // 告知用户名不存在
            throw BaseException.of("usernameNotExist", "用户名不存在");
        }

        // 判断密码是否正确
        if (!new KantbootPassword().matches(password, byUsername.getPassword())) {
            // 告知密码错误
            throw BaseException.of("passwordError", "密码错误");
        }


        return new LoginVO()
                .setToken(tokenService.generateToken(byUsername.getId()))
                .setUserInfo(entityToMap(byUsername));
    }

    @Override
    public LoginVO loginByPhoneAndPassword(String phone, String password) {
        // 去除空格
        phone = phone.trim();
        password = password.trim();

        // 判断用户名是否存在
        SysUser by = repository.findByPhone(phone);
        if (by == null) {
            // 告知用户名不存在
            // TODO 国际化翻译
            throw BaseException.of("phoneNotExist", "手机号不存在");
        }

        // 判断密码是否存在
        if (StrUtil.isBlank(by.getPassword())) {
            // 没有设置密码
            throw BaseException.of("passwordNotSet", "密码未设置");
        }

        // 判断密码是否正确
        if (!new KantbootPassword().matches(password, by.getPassword())) {
            // 告知密码错误
            throw BaseException.of("passwordError", "密码错误");
        }


        return new LoginVO()
                .setToken(tokenService.generateToken(by.getId()))
                .setUserInfo(entityToMap(by));
    }

    @Override
    public LoginVO securityLoginByPhoneAndPassword(String phone, String password) {

        // 去除空格
        phone = phone.trim();
        password = password.trim();
        // 获取密钥和密码部分，用“&&”分割
        String[] split = phone.split("&&");
        if (split.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey = split[0];
        String username2 = split[1];
        // 获取私钥
        String privateKey = getRsaPrivateKey(publicKey);
        // 解密
        RSA rsa = new RSA(privateKey, publicKey);
        String username3 = rsa.decryptStr(username2, KeyType.PrivateKey);

        // 解密密码
        String[] split2 = password.split("&&");
        if (split2.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey2 = split2[0];
        String password2 = split2[1];
        // 获取私钥
        String privateKey2 = getRsaPrivateKey(publicKey2);
        // 解密
        RSA rsa2 = new RSA(privateKey2, publicKey2);
        String password3 = rsa2.decryptStr(password2, KeyType.PrivateKey);
        return loginByPhoneAndPassword(username3, password3);
    }

    @Override
    public LoginVO loginByEmailAndPassword(String email, String password) {
        // 去除空格
        email = email.trim();
        password = password.trim();

        // 判断用户名是否存在
        SysUser by = repository.findByEmail(email);
        if (by == null) {
            // 告知用户名不存在
            // TODO 国际化翻译
            throw BaseException.of("emailNotExist", "邮箱不存在");
        }

        // 判断密码是否存在
        if (StrUtil.isBlank(by.getPassword())) {
            // 没有设置密码
            throw BaseException.of("passwordNotSet", "密码未设置");
        }


        if (!new KantbootPassword().matches(password, by.getPassword())) {
            // 告知密码错误
            throw BaseException.of("passwordError", "密码错误");
        }
        return new LoginVO()
                .setToken(tokenService.generateToken(by.getId()))
                .setUserInfo(entityToMap(by));
    }

    @Override
    public LoginVO securityLoginByEmailAndPassword(String email, String password) {
        // 去除空格
        email = email.trim();
        password = password.trim();
        // 获取密钥和密码部分，用“&&”分割
        String[] split = email.split("&&");
        if (split.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey = split[0];
        String username2 = split[1];
        // 获取私钥
        String privateKey = getRsaPrivateKey(publicKey);
        // 解密
        RSA rsa = new RSA(privateKey, publicKey);
        String username3 = rsa.decryptStr(username2, KeyType.PrivateKey);

        // 解密密码
        String[] split2 = password.split("&&");
        if (split2.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey2 = split2[0];
        String password2 = split2[1];
        // 获取私钥
        String privateKey2 = getRsaPrivateKey(publicKey2);
        // 解密
        RSA rsa2 = new RSA(privateKey2, publicKey2);
        String password3 = rsa2.decryptStr(password2, KeyType.PrivateKey);
        return loginByEmailAndPassword(username3, password3);
    }


    /**
     * 邮箱+验证码登录（安全）
     * @param email 邮箱（加密）
     * @param varCode 验证码（加密）
     * @return token
     */
    @Override
    public LoginVO securityLoginByEmailAndVarCode(String email, String varCode) {
        // 去除空格
        email = email.trim();
        varCode = varCode.trim();
        // 获取密钥和密码部分，用“&&”分割
        String[] split = email.split("&&");
        if (split.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey = split[0];
        String username2 = split[1];
        // 获取私钥
        String privateKey = getRsaPrivateKey(publicKey);
        // 解密
        RSA rsa = new RSA(privateKey, publicKey);
        String username3 = rsa.decryptStr(username2, KeyType.PrivateKey);
        // 解密验证码
        String[] split2 = varCode.split("&&");
        if (split2.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey2 = split2[0];
        String varCode2 = split2[1];
        // 获取私钥
        String privateKey2 = getRsaPrivateKey(publicKey2);
        // 解密
        RSA rsa2 = new RSA(privateKey2, publicKey2);
        String varCode3 = rsa2.decryptStr(varCode2, KeyType.PrivateKey);
        return loginByEmailAndVarCode(username3, varCode3);
    }


    @Override
    public LoginVO loginBySmsAndVarCode(String phone, String varCode) {
        // 去除空格
        phone = phone.trim();
        varCode = varCode.trim();
        // 判断验证码是否正确
        if (!apiVarCodeService.checkSmsCode(new ApiVarCode().setTo(phone)
                        .setSceneCode("login")
                .setValue(varCode))) {
            // 告知验证码错误
            throw BaseException.of("varCodeError", "验证码错误");
        }
        // 判断用户名是否存在
        SysUser by = repository.findByPhone(phone);
        if (by != null) {
            return new LoginVO()
                    .setToken(tokenService.generateToken(by.getId()))
                    .setUserInfo(entityToMap(by));
        }
        // 用户不存在，创建用户
        SysUser sysUser = new SysUser();
        sysUser.setPhone(phone);
        sysUser.setIsTemporary(true);
        sysUser.setIsInit(false);
        SysUser result = repository.save(sysUser);
        // 保存用户角色
        String defaultRoleCode = getDefaultRoleCode();
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleCode(defaultRoleCode);
        userRoleRepository.save(sysUserRole.setUserId(result.getId()));
        return new LoginVO()
                .setToken(tokenService.generateToken(result.getId()))
                .setUserInfo(entityToMap(resultSetRoleCode(result, defaultRoleCode)));
    }

    @Override
    public LoginVO securityLoginBySmsAndVarCode(String phone, String varCode) {
        // 去除空格
        phone = phone.trim();
        varCode = varCode.trim();
        // 获取密钥和密码部分，用“&&”分割
        String[] split = phone.split("&&");
        if (split.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey = split[0];
        String username2 = split[1];
        // 获取私钥
        String privateKey = getRsaPrivateKey(publicKey);
        // 解密
        RSA rsa = new RSA(privateKey, publicKey);
        String username3 = rsa.decryptStr(username2, KeyType.PrivateKey);
        // 解密验证码
        String[] split2 = varCode.split("&&");
        if (split2.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey2 = split2[0];
        String varCode2 = split2[1];
        // 获取私钥
        String privateKey2 = getRsaPrivateKey(publicKey2);
        // 解密
        RSA rsa2 = new RSA(privateKey2, publicKey2);
        String varCode3 = rsa2.decryptStr(varCode2, KeyType.PrivateKey);
        return loginBySmsAndVarCode(username3, varCode3);
    }

    /**
     * 邮箱+验证码登录
     * @param email 邮箱
     * @param varCode 验证码
     * @return token
     */
    @Override
    public LoginVO loginByEmailAndVarCode(String email, String varCode) {
        // 去除空格
        email = email.trim();
        // 判断验证码是否正确
        if (!apiVarCodeService.checkEmailCode(new ApiVarCode().setTo(email)
                        .setSceneCode("login")
                .setValue(varCode))) {
            // 告知验证码错误
            throw BaseException.of("varCodeError", "验证码错误");
        }
        // 判断用户名是否存在
        SysUser by = repository.findByEmail(email);
        if (by != null) {
            return new LoginVO()
                    .setToken(tokenService.generateToken(by.getId()))
                    .setUserInfo(entityToMap(by));
        }
        // 用户不存在，创建用户
        SysUser sysUser = new SysUser();
        sysUser.setEmail(email);
        sysUser.setIsTemporary(true);
        sysUser.setIsInit(false);
        SysUser result = repository.save(sysUser);
        // 保存用户角色
        String defaultRoleCode = getDefaultRoleCode();
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleCode(defaultRoleCode);
        userRoleRepository.save(sysUserRole.setUserId(result.getId()));
        return new LoginVO()
                .setToken(tokenService.generateToken(result.getId()))
                .setUserInfo(entityToMap(resultSetRoleCode(result, defaultRoleCode)));
    }

    @Override
    public Map<String, Object> skipBind() {
        Long selfId = getSelfId();
        SysUser sysUser = repository.findById(selfId).orElseThrow(() -> BaseException.of("notLogin", "未登录"));
        sysUser.setIsTemporary(false);
        repository.save(sysUser);
        return getSelf();
    }

    @Override
    public LoginVO register(String username, String password) {
        // 计时1
        long startTime1 = System.currentTimeMillis();

        // 去除空格
        username = username.trim();
        password = password.trim();

        // 判断用户名是否存在
        SysUser byUsername = repository.findByUsername(username);
        if (byUsername != null) {
            // 告知用户名已存在
            throw BaseException.of("usernameRepeat", "用户名已存在");
        }

        // 创建用户
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);

        // 密码加密开始时间
        long passwordEncodeStartTime = System.currentTimeMillis();
        sysUser.setPassword(new KantbootPassword().encode(password));
        // 密码加密结束时间
        long passwordEncodeEndTime = System.currentTimeMillis();
        log.info("密码加密耗时：{}ms", passwordEncodeEndTime - passwordEncodeStartTime);
        // 保存用户
        SysUser result = repository.save(sysUser);
        // 计时1结束
        long endTime1 = System.currentTimeMillis();
        log.info("保存用户耗时：{}ms", endTime1 - startTime1);

        // 计时2
        long startTime2 = System.currentTimeMillis();
        // 保存用户角色
        String defaultRoleCode = getDefaultRoleCode();
        log.info("默认角色:{}", defaultRoleCode);
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleCode(defaultRoleCode);
        userRoleRepository.save(sysUserRole);


        // 重新复制一份，防止修改
        long startTime4 = System.currentTimeMillis();
        SysUser sysUserVO = resultSetRoleCode(result, defaultRoleCode);
        // 计时4结束
        long endTime4 = System.currentTimeMillis();
        log.info("重新复制一份耗时：{}ms", endTime4 - startTime4);

        return new LoginVO()
                .setToken(tokenService.generateToken(result.getId()))
                .setUserInfo(entityToMap(sysUserVO));
    }


    private SysUser resultSetRoleCode(SysUser vo, String defaultRoleCode) {
        SysUser result = BeanUtil.copyProperties(vo, SysUser.class);
        List<SysUserRole> objects = new ArrayList<>();
        SysUserRole defaultRole = new SysUserRole().setRoleCode(defaultRoleCode).setVisible(
                roleService.getByCode(defaultRoleCode).getVisible()
        );
        userRoleRepository.save(defaultRole.setUserId(vo.getId()));
        objects.add(defaultRole);
        result.setRoleList(objects);
        return result;
    }



    @SneakyThrows
    @Override
    public String getRsaPublicKey() {
        // 获取ip，用于限制请求次数
        String ip = httpRequestHeaderUtil.getIp();
        String s = redisUtil.get("rsa::requestCount:" + ip);
        if (StrUtil.isBlank(s)) {
            // 第一次请求，设置请求次数为0
            redisUtil.setEx("rsa::requestCount:" + ip, "0", 2, TimeUnit.MINUTES);
            s = "0";
        }
        // 获取请求次数
        Integer requestCount = Integer.parseInt(redisUtil.get("rsa::requestCount:" + ip));
        if (requestCount > 50) {
            Thread.sleep(1000);
        }
        if (requestCount > 100) {
            Thread.sleep(2000);
        }
        if (requestCount > 200) {
            Thread.sleep(2000);
        }
        if (requestCount > 300) {
            Thread.sleep(2000);
        }
        if (requestCount > 400) {
            String redisLockKey = "res:lock:"+httpRequestHeaderUtil.getUserAgent()+":"+ httpRequestHeaderUtil.getIp();
            // 获取锁
            if(redisUtil.lock(redisLockKey, 10, TimeUnit.SECONDS)){
                // 被占线，请稍后再试
                throw BaseException.of("requestTooMany", "请求过于频繁，请稍后再试");
            }
            Thread.sleep(2000);
        }

        // 请求次数加一
        redisUtil.setEx("rsa::requestCount:" + ip, String.valueOf(Integer.parseInt(s) + 1),100,
                TimeUnit.SECONDS);


        RSA rsa = new RSA();
        String publicKeyBase64 = rsa.getPublicKeyBase64();
        String privateKeyBase64 = rsa.getPrivateKeyBase64();
        // 保存到 redis，30分钟过期
        redisUtil.setEx("rsa::publicKey:" + publicKeyBase64, privateKeyBase64, 30, TimeUnit.MINUTES);
        return publicKeyBase64;
    }

    @Override
    public String getRsaPrivateKey(String publicKey) {
        String result = redisUtil.get("rsa::publicKey:" + publicKey);
        if (StrUtil.isBlank(result)) {
            // 未找到私钥
            throw BaseException.of("privateKeyNotFound", "未找到私钥");
        }
        return result;
    }

    @Override
    public Map<String, Object> initBindBySmsAndVarCode(String phone, String varCode) {
        Long selfId = getSelfId();
        SysUser self = repository.findById(selfId).orElseThrow(() -> BaseException.of("notLogin", "未登录"));
        if(self.getPhone()!=null){
            // 已经绑定过了
            throw BaseException.of("alreadyBind", "不可重复绑定");
        }

        // 去除空格
        phone = phone.trim();
        varCode = varCode.trim();

        // 检查验证码是否错误
        if(!apiVarCodeService.checkSmsCode(
                new ApiVarCode().setTo(phone)
                        .setSceneCode("bind")
                        .setTypeCode("sms")
                        .setValue(varCode))){
            throw BaseException.of("varCodeError", "验证码错误");
        }

        // 检查手机号码是否已经被绑定
        SysUser byPhone = repository.findByPhone(phone);
        if(byPhone!=null){
            // 判断用户邮箱是否为空
            if(self.getEmail()!=null){
                byPhone.setEmail(self.getEmail());
            }
            // 将第三方账号绑定到已有账号

            List<SysUserThirdParty> fromThird = userThirdPartyRepository.findByUserId(self.getId());
            List<SysUserThirdParty> toThird = userThirdPartyRepository.findByUserId(byPhone.getId());
            // 检查是否有双方都有的第三方用户
            for (SysUserThirdParty from : fromThird) {
                for (SysUserThirdParty to : toThird) {
                    if(from.getThirdPartyCode().equals(to.getThirdPartyCode())&&from.getKey().equals(to.getKey())){
                        throw BaseException.of("thirdPartyConflict", "有重复绑定的第三方");
                    }
                }
            }
            for (SysUserThirdParty sysUserThirdParty : fromThird) {
                sysUserThirdParty.setUserId(byPhone.getId());
            }
            userThirdPartyRepository.saveAll(fromThird);
            tokenService.setTokenToUser(tokenService.getSelfToken(), byPhone.getId());
            byPhone.setIsTemporary(false);
            byPhone = repository.save(byPhone);
            // 删除临时用户
            repository.deleteById(self.getId());
            return entityToMap(byPhone);
        }

        // 如果没绑定过手机号，就绑定
        self.setPhone(phone);
        self.setIsTemporary(false);
        return entityToMap(repository.save(self));
    }

    @Override
    public Map<String, Object> securityInitBindBySmsAndVarCode(String phone, String varCode) {
        // 去除空格
        phone = phone.trim();
        varCode = varCode.trim();
        // 获取密钥和密码部分，用“&&”分割
        String[] split = phone.split("&&");
        if (split.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey = split[0];
        String username2 = split[1];
        // 获取私钥
        String privateKey = getRsaPrivateKey(publicKey);
        // 解密
        RSA rsa = new RSA(privateKey, publicKey);
        String username3 = rsa.decryptStr(username2, KeyType.PrivateKey);
        // 解密验证码
        String[] split2 = varCode.split("&&");
        if (split2.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey2 = split2[0];
        String varCode2 = split2[1];
        // 获取私钥
        String privateKey2 = getRsaPrivateKey(publicKey2);
        // 解密
        RSA rsa2 = new RSA(privateKey2, publicKey2);
        String varCode3 = rsa2.decryptStr(varCode2, KeyType.PrivateKey);
        return initBindBySmsAndVarCode(username3, varCode3);
    }

    @Override
    public Map<String, Object> initBindByEmailAndVarCode(String email, String varCode) {
        Long selfId = getSelfId();
        SysUser self = repository.findById(selfId).orElseThrow(() -> BaseException.of("notLogin", "未登录"));
        if(self.getEmail()!=null){
            // 已经绑定过了
            throw BaseException.of("alreadyBind", "不可重复绑定");
        }

        // 去除空格
        email = email.trim();
        varCode = varCode.trim();

        // 检查验证码是否错误
        if(!apiVarCodeService.checkEmailCode(
                new ApiVarCode().setTo(email)
                        .setSceneCode("bind")
                        .setTypeCode("email")
                        .setValue(varCode))){
            throw BaseException.of("varCodeError", "验证码错误");
        }

        // 检查邮箱是否已经被绑定
        SysUser byEmail = repository.findByEmail(email);
        if(byEmail!=null){
            // 判断用户手机号码是否为空
            if(self.getPhone()!=null){
                byEmail.setPhone(self.getPhone());
            }
            // 将第三方账号绑定到已有账号

            List<SysUserThirdParty> fromThird = userThirdPartyRepository.findByUserId(self.getId());
            List<SysUserThirdParty> toThird = userThirdPartyRepository.findByUserId(byEmail.getId());
            // 检查是否有双方都有的第三方用户
            for (SysUserThirdParty from : fromThird) {
                for (SysUserThirdParty to : toThird) {
                    if(from.getThirdPartyCode().equals(to.getThirdPartyCode())&&from.getKey().equals(to.getKey())){
                        throw BaseException.of("thirdPartyConflict", "有重复绑定的第三方");
                    }
                }
            }

            byEmail.setIsTemporary(false);
            for (SysUserThirdParty sysUserThirdParty : fromThird) {
                sysUserThirdParty.setUserId(byEmail.getId());
            }
            userThirdPartyRepository.saveAll(fromThird);
            tokenService.setTokenToUser(tokenService.getSelfToken(), byEmail.getId());
            byEmail = repository.save(byEmail);
            // 删除临时用户
            repository.deleteById(self.getId());
            return entityToMap(byEmail);
        }

        // 如果没绑定过邮箱，就绑定
        self.setEmail(email);
        // 设置为非临时用户
        self.setIsTemporary(false);
        return entityToMap(repository.save(self));
    }

    @Override
    public Map<String, Object> securityInitBindByEmailAndVarCode(String email, String varCode) {
        // 去除空格
        email = email.trim();
        varCode = varCode.trim();
        // 获取密钥和密码部分，用“&&”分割
        String[] split = email.split("&&");
        if (split.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey = split[0];
        String username2 = split[1];
        // 获取私钥
        String privateKey = getRsaPrivateKey(publicKey);
        // 解密
        RSA rsa = new RSA(privateKey, publicKey);
        String username3 = rsa.decryptStr(username2, KeyType.PrivateKey);
        // 解密验证码
        String[] split2 = varCode.split("&&");
        if (split2.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey2 = split2[0];
        String varCode2 = split2[1];
        // 获取私钥
        String privateKey2 = getRsaPrivateKey(publicKey2);
        // 解密
        RSA rsa2 = new RSA(privateKey2, publicKey2);
        String varCode3 = rsa2.decryptStr(varCode2, KeyType.PrivateKey);
        return initBindByEmailAndVarCode(username3, varCode3);
    }

    /**
     * 安全邮箱+验证码绑定
     * @param username 用户名(加密)
     * @param password 密码(加密)
     * @return
     */


    @Override
    public LoginVO securityLogin(String username, String password) {

        // 去除空格
        username = username.trim();
        password = password.trim();
        // 获取密钥和密码部分，用“&&”分割
        String[] split = username.split("&&");
        if (split.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey = split[0];
        String username2 = split[1];
        // 获取私钥
        String privateKey = getRsaPrivateKey(publicKey);
        // 解密
        RSA rsa = new RSA(privateKey, publicKey);
        String username3 = rsa.decryptStr(username2, KeyType.PrivateKey);

        // 解密密码
        String[] split2 = password.split("&&");
        if (split2.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey2 = split2[0];
        String password2 = split2[1];
        // 获取私钥
        String privateKey2 = getRsaPrivateKey(publicKey2);
        // 解密
        RSA rsa2 = new RSA(privateKey2, publicKey2);
        String password3 = rsa2.decryptStr(password2, KeyType.PrivateKey);

        return login(username3, password3);
    }

    @Override
    public LoginVO securityRegister(String username, String password) {
        // 去除空格
        username = username.trim();
        password = password.trim();
        // 获取密钥和密码部分，用“&&”分割
        String[] split = username.split("&&");
        if (split.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey = split[0];
        String username2 = split[1];
        // 获取私钥
        String privateKey = getRsaPrivateKey(publicKey);
        // 解密
        RSA rsa = new RSA(privateKey, publicKey);
        String username3 = rsa.decryptStr(username2, KeyType.PrivateKey);

        // 解密密码
        String[] split2 = password.split("&&");
        if (split2.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey2 = split2[0];
        String password2 = split2[1];
        // 获取私钥
        String privateKey2 = getRsaPrivateKey(publicKey2);
        // 解密
        RSA rsa2 = new RSA(privateKey2, publicKey2);
        String password3 = rsa2.decryptStr(password2, KeyType.PrivateKey);

        return register(username3, password3);
    }

    @Override
    public SysUserAttribute setAttributeValue(Long userId, String attributeCode, String value) {
        return null;
    }

    @Override
    public void addAttributeDetail(Long userId, String attributeCode, List<SysUserAttributeDetail> details) {
    }

    @Override
    public void setAttributeDetail(Long userId, String attributeCode, List<SysUserAttributeDetail> details) {
    }


    @Override
    public void online(Long userId) {
        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setUserId(userId);
        sysUserOnline.setOnlineStatusCode(SysUserOnline.ONLINE_STATUS_CODE_ONLINE);
        sysUserOnline.setGmtLastEnter(System.currentTimeMillis());
        sysUserOnline.setOnline(true);
        sysUserOnline.setGmtExpire(System.currentTimeMillis()+1000*60*3);
        onlineRepository.save(sysUserOnline);
    }

    @Override
    public void offline(Long userId) {
        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setUserId(userId);
        sysUserOnline.setOnlineStatusCode(SysUserOnline.ONLINE_STATUS_CODE_OFFLINE);
        sysUserOnline.setGmtLastLeave(System.currentTimeMillis());
        sysUserOnline.setOnline(false);
        onlineRepository.save(sysUserOnline);
    }

    @Override
    public Boolean isUsernameExist(String username) {
        SysUser byUsername = repository.findByUsername(username);
        return byUsername != null;
    }

    @Override
    public void addRole(Long userId, List<String> roleCodes) {
        SysUser sysUser = repository.findById(userId).orElseThrow(() -> BaseException.of("userNotExist", "用户不存在"));
        List<SysUserRole> roleList = sysUser.getRoleList();
        if (roleList == null) {
            roleList = new ArrayList<>();
        }
        List<String> roleCodeListOfAdd = new ArrayList<>();
        // 如果已经有了，就不添加了
        for (String roleCode : roleCodes) {
            boolean exist = false;
            for (SysUserRole sysUserRole : roleList) {
                if (sysUserRole.getRoleCode().equals(roleCode)) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                roleCodeListOfAdd.add(roleCode);
            }
        }


        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        for (String roleCode : roleCodeListOfAdd) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleCode(roleCode);
            sysUserRoleList.add(sysUserRole);
        }
        userRoleRepository.saveAll(sysUserRoleList);
    }

    @Override
    public void setRole(Long userId, List<String> roleCodes) {
        List<SysUserRole> notAdminByUserId = userRoleRepository.findNotAdminByUserId(userId);
        List<SysUserRole> roleList = new ArrayList<>();
        for (SysUserRole sysUserRole : notAdminByUserId) {
            if(sysUserRole.getVisible()==null||sysUserRole.getVisible()){
                roleList.add(sysUserRole);
            }
        }

        // 删除所有角色，除了admin
        userRoleRepository.deleteAll(roleList);
        // 添加角色
        addRole(userId, roleCodes);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // 去除空格
        oldPassword = oldPassword.trim();
        newPassword = newPassword.trim();

        // 判断用户名是否存在
        String selfToken = tokenService.getSelfToken();
        Long userIdByToken = tokenService.getUserIdByToken(selfToken);
        SysUser self = repository.findById(userIdByToken).orElseThrow(() -> BaseException.of("userNotExist", "用户不存在"));
        // 判断密码是否正确
        if (!new KantbootPassword().matches(oldPassword, self.getPassword())) {
            // 告知密码错误
            throw BaseException.of("passwordError", "密码错误");
        }

        // 密码加密开始时间
        long passwordEncodeStartTime = System.currentTimeMillis();
        self.setPassword(new KantbootPassword().encode(newPassword));
        // 密码加密结束时间
        long passwordEncodeEndTime = System.currentTimeMillis();
        log.info("密码加密耗时：{}ms", passwordEncodeEndTime - passwordEncodeStartTime);
        // 保存用户
        repository.save(self);
    }

    @Override
    public SysUser create(SysUser entity) {
        SysUser userBean = BeanUtil.copyProperties(entity, SysUser.class);

        entity.setId(null);
        entity.setRoleList(null);
        entity.setIsInit(false);
        SysUser save = repository.save(entity);

        // 如果没有角色，就添加默认角色
        if (userBean.getRoleList() == null || userBean.getRoleList().isEmpty()) {
            List<SysUserRole> sysUserRoleList = new ArrayList<>();
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(save.getId());
            sysUserRole.setRoleCode(getDefaultRoleCode());
            sysUserRoleList.add(sysUserRole);
            userRoleRepository.saveAll(sysUserRoleList);
        }else{
            List<SysUserRole> sysUserRoleList = new ArrayList<>();
            for (SysUserRole sysUserRole : userBean.getRoleList()) {
                sysUserRole.setUserId(save.getId());
                sysUserRoleList.add(sysUserRole);
                userRoleRepository.save(sysUserRole);
            }
        }

        return repository.save(entity);
    }

    @Override
    public void initSelfInfo(SysUserInitInfoDTO dto) {
        SysUser newUser = repository.findById(getSelfId()).orElseThrow(() -> BaseException.of("notLogin", "未登录"));
        // 设置性别
        newUser.setGenderCode(dto.getGenderCode());
        // 设置昵称
        newUser.setNickname(dto.getNickname());
        // 设置头像
        newUser.setFileIdOfAvatar(dto.getFileIdOfAvatar());
        // 设置生日
        newUser.setGmtBirthday(dto.getGmtBirthday());
        // 设置自我介绍
        newUser.setIntroduction(dto.getIntroduction());
        // 设置SM倾向
        newUser.setSadomasochismCode(dto.getSadomasochismCode());
        log.info("修改个人信息：{}", newUser);
        repository.save(newUser);
    }

    @Override
    public PageResult getBodyData(PageParam<SysUserSearchDTO> pageParam){
        Page<SysUser> bodyData = repository.getBodyData(pageParam.getData(), pageParam.getPageable());
        List<Map<String, Object>> mapList = new ArrayList<>();
        bodyData.getContent().forEach(item->{
            mapList.add(entityToMap(item));
        });
        PageResult pageResult = PageResult.of(bodyData);
        pageResult.setContent(mapList);
        return pageResult;
    }

    @Override
    public LoginVO phoneRegisterWithVarCode(String phone, String password, String varCode) {
        // 去除空格
        phone = phone.trim();
        password = password.trim();
        varCode = varCode.trim();

        // 判断用户名是否存在
        SysUser by = repository.findByPhone(phone);
        if (by != null) {
            // 告知用户名已存在
            throw BaseException.of("phoneRepeat", "手机号码已存在");
        }

        // 检查验证码是否错误
        if(!apiVarCodeService.checkSmsCode(
                new ApiVarCode().setTo(phone)
                        .setSceneCode("register")
                        .setTypeCode("sms")
                        .setValue(varCode))){
            throw BaseException.of("varCodeError", "验证码错误");
        }

        // 创建用户
        SysUser sysUser = new SysUser();
        sysUser.setPhone(phone);

        // 密码加密开始时间
        long passwordEncodeStartTime = System.currentTimeMillis();
        sysUser.setPassword(new KantbootPassword().encode(password));
        // 密码加密结束时间
        long passwordEncodeEndTime = System.currentTimeMillis();
        log.info("密码加密耗时：{}ms", passwordEncodeEndTime - passwordEncodeStartTime);
        // 保存用户
        SysUser result = repository.save(sysUser);
        // 计时1结束
        long endTime1 = System.currentTimeMillis();
        log.info("保存用户耗时：{}ms", endTime1 - passwordEncodeEndTime);

        // 计时2
        long startTime2 = System.currentTimeMillis();
        // 保存用户角色
        String defaultRoleCode = getDefaultRoleCode();
        log.info("默认角色:{}", defaultRoleCode);
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleCode(defaultRoleCode);
        userRoleRepository.save(sysUserRole);
        return new LoginVO()
                .setToken(tokenService.generateToken(result.getId()))
                .setUserInfo(entityToMap(resultSetRoleCode(result, defaultRoleCode)));
    }

    @Override
    public LoginVO securityPhoneRegisterWithVarCode(String phone, String password, String varCode) {
        phone = phone.trim();
        password = password.trim();
        varCode = varCode.trim();
        // 获取密钥和密码部分，用“&&”分割
        String[] split = phone.split("&&");
        if (split.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey = split[0];
        String username2 = split[1];
        // 获取私钥
        String privateKey = getRsaPrivateKey(publicKey);
        // 解密用户名
        RSA rsa = new RSA(privateKey, publicKey);
        String username3 = rsa.decryptStr(username2, KeyType.PrivateKey);
        String[] split2 = password.split("&&");
        if (split2.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey2 = split2[0];
        String password2 = split2[1];
        // 获取私钥
        String privateKey2 = getRsaPrivateKey(publicKey2);
        // 解密密码
        RSA rsa2 = new RSA(privateKey2, publicKey2);
        String password3 = rsa2.decryptStr(password2, KeyType.PrivateKey);
        String[] split3 = varCode.split("&&");
        if (split3.length != 2) {
            // 密钥错误
            throw BaseException.of("keyError", "密钥错误");
        }
        String publicKey3 = split3[0];
        String varCode2 = split3[1];
        // 获取私钥
        String privateKey3 = getRsaPrivateKey(publicKey3);
        // 解密验证码
        String varCode3 = new RSA(privateKey3, publicKey3).decryptStr(varCode2, KeyType.PrivateKey);
        return phoneRegisterWithVarCode(username3, password3, varCode3);
    }
}