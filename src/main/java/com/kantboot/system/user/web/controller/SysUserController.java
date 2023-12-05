package com.kantboot.system.user.web.controller;

import com.kantboot.system.user.domain.dto.SysUserInitInfoDTO;
import com.kantboot.system.user.domain.vo.LoginVO;
import com.kantboot.system.user.service.ISysUserService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户控制器
 * @author 方某方
 */
@RestController
@RequestMapping("/system-user-web/user")
public class SysUserController {

    @Resource
    private ISysUserService service;

    /**
     * 获取自身的信息
     * @return 用户
     */
    @PostMapping("/getSelf")
    public RestResult<Map<String,Object>> getSelf(){
        return RestResult.success(service.getSelf(),"getSuccess","获取成功");
    }

    /**
     * 根据id获取用户
     * @param id 用户id
     * @return 用户
     */
    @PostMapping("/getById")
    public RestResult<Map<String,Object>> getById(
            @RequestParam("id") Long id) {
        return RestResult.success(service.getById(id),"getSuccess","获取成功");
    }

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    @PostMapping("/register")
    public RestResult<LoginVO> register(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        return RestResult.success(service.register(username,password),"registerSuccess","注册成功");
    }

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    @PostMapping("/login")
    public RestResult<LoginVO> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        return RestResult.success(service.login(username,password),"loginSuccess","登录成功");
    }

    /**
     * 手机号码+密码登录
     * loginByPhoneAndPassword
     * @param phone 手机号码
     * @param password 密码
     * @return 结果
     */
    @PostMapping("/loginByPhoneAndPassword")
    public RestResult<LoginVO> loginByPhoneAndPassword(
            @RequestParam("phone") String phone,
            @RequestParam("password") String password) {
        return RestResult.success(service.loginByPhoneAndPassword(phone,password),"loginSuccess","登录成功");
    }

    /**
     * 手机号码+密码登录（安全）
     * securityLoginByPhoneAndPassword
     * @param phone 手机号码
     * @param password 密码
     * @return 结果
     */
    @PostMapping("/securityLoginByPhoneAndPassword")
    public RestResult<LoginVO> securityLoginByPhoneAndPassword(
            @RequestParam("phone") String phone,
            @RequestParam("password") String password) {
        return RestResult.success(service.securityLoginByPhoneAndPassword(phone,password),"loginSuccess","登录成功");
    }

    /**
     * 邮箱+密码登录
     * loginByEmailAndPassword
     * @param email 邮箱
     * @param password 密码
     * @return 结果
     */
    @PostMapping("/loginByEmailAndPassword")
    public RestResult<LoginVO> loginByEmailAndPassword(
            @RequestParam("email") String email,
            @RequestParam("password") String password) {
        return RestResult.success(service.loginByEmailAndPassword(email,password),"loginSuccess","登录成功");
    }

    /**
     * 邮箱+密码登录（安全）
     * securityLoginByEmailAndPassword
     * @param email 邮箱
     * @param password 密码
     * @return 结果
     */
    @PostMapping("/securityLoginByEmailAndPassword")
    public RestResult<LoginVO> securityLoginByEmailAndPassword(
            @RequestParam("email") String email,
            @RequestParam("password") String password) {
        return RestResult.success(service.securityLoginByEmailAndPassword(email,password),"loginSuccess","登录成功");
    }

    /**
     * 邮箱+验证码登录
     */
    @PostMapping("/loginByEmailAndVarCode")
    public RestResult<LoginVO> loginByEmailAndVarCode(
            @RequestParam("email") String email,
            @RequestParam("varCode") String varCode) {
        return RestResult.success(service.loginByEmailAndVarCode(email,varCode),"loginSuccess","登录成功");
    }

    /**
     * 邮箱+验证码登录（安全）
     * securityLoginByEmailAndVarCode
     * @param email 邮箱（加密）
     * @param varCode 验证码（加密）
     * @return 结果
     */
    @PostMapping("/securityLoginByEmailAndVarCode")
    public RestResult<LoginVO> securityLoginByEmailAndVarCode(
            @RequestParam("email") String email,
            @RequestParam("varCode") String varCode) {
        return RestResult.success(service.securityLoginByEmailAndVarCode(email,varCode),"loginSuccess","登录成功");
    }

    /**
     * 手机号码+验证码注册
     * loginByPhoneAndVarCode
     * @param phone 手机号码
     * @param varCode 验证码
     * @return 结果
     */
    @PostMapping("/loginBySmsAndVarCode")
    public RestResult<LoginVO> loginBySmsAndVarCode(
            @RequestParam("phone") String phone,
            @RequestParam("varCode") String varCode) {
        return RestResult.success(service.loginBySmsAndVarCode(phone,varCode),"loginSuccess","登录成功");
    }

    /**
     * 手机号码+验证码注册（安全）
     * securityLoginBySmsAndVarCode
     * @param phone 手机号码
     * @param varCode 验证码
     * @return 结果
     */
    @PostMapping("/securityLoginBySmsAndVarCode")
    public RestResult<LoginVO> securityLoginBySmsAndVarCode(
            @RequestParam("phone") String phone,
            @RequestParam("varCode") String varCode) {
        return RestResult.success(service.securityLoginBySmsAndVarCode(phone,varCode),"loginSuccess","登录成功");
    }

    /**
     * 跳过绑定
     */
    @PostMapping("/skipBind")
    public RestResult<Map<String, Object>> skipBind() {
        return RestResult.success(service.skipBind(),"skipSuccess","跳过成功");
    }

    /**
     * 手机号+验证码初始化绑定
     */
    @PostMapping("/initBindBySmsAndVarCode")
    public RestResult<Map<String, Object>> initBindBySmsAndVarCode(
            @RequestParam("phone") String phone,
            @RequestParam("varCode") String varCode) {
        return RestResult.success(service.initBindBySmsAndVarCode(phone,varCode),"initSuccess","初始化成功");
    }

    /**
     * 手机号+验证码初始化绑定（安全）
     */
    @PostMapping("/securityInitBindBySmsAndVarCode")
    public RestResult<Map<String, Object>> securityInitBindBySmsAndVarCode(
            @RequestParam("phone") String phone,
            @RequestParam("varCode") String varCode) {
        return RestResult.success(service.securityInitBindBySmsAndVarCode(phone,varCode),"initSuccess","初始化成功");
    }

    /**
     * 邮箱+验证码初始化绑定
     */
    @PostMapping("/initBindByEmailAndVarCode")
    public RestResult<Map<String, Object>> initBindByEmailAndVarCode(
            @RequestParam("email") String email,
            @RequestParam("varCode") String varCode) {
        return RestResult.success(service.initBindByEmailAndVarCode(email,varCode),"initSuccess","初始化成功");
    }

    /**
     * 邮箱+验证码初始化绑定（安全）
     */
    @PostMapping("/securityInitBindByEmailAndVarCode")
    public RestResult<Map<String, Object>> securityInitBindByEmailAndVarCode(
            @RequestParam("email") String email,
            @RequestParam("varCode") String varCode) {
        return RestResult.success(service.securityInitBindByEmailAndVarCode(email,varCode),"initSuccess","初始化成功");
    }


    /**
     * 获取rsa公钥
     * @return 结果
     */
    @PostMapping("/getRsaPublicKey")
    public RestResult<String> getRsaPublicKey() {
        return RestResult.success(service.getRsaPublicKey(),"generateKeySuccess","生成成功");
    }

    /**
     * 安全登录
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    @PostMapping("/securityLogin")
    public RestResult<LoginVO> securityLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        return RestResult.success(service.securityLogin(username,password),"loginSuccess","登录成功");
    }

    /**
     * 安全注册
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    @PostMapping("/securityRegister")
    public RestResult<LoginVO> securityRegister(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        return RestResult.success(service.securityRegister(username, password),"registerSuccess","注册成功");
    }

    /**
     * 修改密码 json
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @PostMapping(value = "/changePassword",consumes = "application/json")
    public RestResult<LoginVO> changePassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword) {
        service.changePassword(oldPassword,newPassword);
        return RestResult.success(null,"changeSuccess","修改成功");
    }

    /**
     * 初始化自身信息
     * @param dto 用户
     */
    @PostMapping("/initSelfInfo")
    public RestResult<LoginVO> initSelfInfo(
            @RequestBody SysUserInitInfoDTO dto) {
        service.initSelfInfo(dto);
        return RestResult.success(null,"initSuccess","初始化成功");
    }

    /**
     * 注册加验证码
     * @param phone 用户名
     * @param password 密码
     * @param varCode 验证码
     * @return 结果
     */
    @PostMapping("/phoneRegisterWithVarCode")
    public RestResult<LoginVO> phoneRegisterWithVarCode(
            @RequestParam("phone") String phone,
            @RequestParam("password") String password,
            @RequestParam("varCode") String varCode) {
        return RestResult.success(service.phoneRegisterWithVarCode(phone,password,varCode),"registerSuccess","注册成功");
    }

    /**
     * 安全注册加验证码
     * @param phone 用户名（加密）
     * @param password 密码（加密）
     * @param varCode 验证码（加密）
     * @return 结果
     */
    @PostMapping("/securityPhoneRegisterWithVarCode")
    public RestResult<LoginVO> securityRegisterWithVarCode(
            @RequestParam("phone") String phone,
            @RequestParam("password") String password,
            @RequestParam("varCode") String varCode) {
        return RestResult.success(service.securityPhoneRegisterWithVarCode(phone,password,varCode),"registerSuccess","注册成功");
    }

}
