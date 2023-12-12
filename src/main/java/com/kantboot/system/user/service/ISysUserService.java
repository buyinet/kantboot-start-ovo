package com.kantboot.system.user.service;

import com.kantboot.system.user.domain.dto.SysUserInitInfoDTO;
import com.kantboot.system.user.domain.dto.SysUserSaveDTOOfDtu;
import com.kantboot.system.user.domain.dto.SysUserSearchDTO;
import com.kantboot.system.user.domain.entity.SysUser;
import com.kantboot.system.user.domain.entity.SysUserAttribute;
import com.kantboot.system.user.domain.entity.SysUserAttributeDetail;
import com.kantboot.system.user.domain.vo.LoginVO;
import com.kantboot.util.core.param.PageParam;
import com.kantboot.util.core.result.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 * @author 方某方
 */
public interface ISysUserService {

    /**
     * 获取默认角色编码
     * @return 默认角色编码
     */
    String getDefaultRoleCode();

    /**
     * 根据用户id获取用户信息
     * @param id 用户id
     * @return 用户信息
     */
    Map<String,Object> getById(Long id);

    /**
     * 根据用户id获取用户信息，并且肯定是自己
     * @param id 用户id
     * @return 用户信息
     */
    Map<String,Object> getSelfById(Long id);

    /**
     * 获取自己的信息
     */
    Map<String,Object> getSelf();

    Long getSelfId();


    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    LoginVO login(String username, String password);

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    LoginVO register(String username, String password);

    /**
     * 手机号码+密码登录
     */
    LoginVO loginByPhoneAndPassword(String phone, String password);

    /**
     * 手机号码+密码登录（安全）
     */
    LoginVO securityLoginByPhoneAndPassword(String phone, String password);

    /**
     * 邮箱+密码登录
     */
    LoginVO loginByEmailAndPassword(String email, String password);

    /**
     * 邮箱+密码登录（安全）
     */
    LoginVO securityLoginByEmailAndPassword(String email, String password);

    /**
     * 邮箱+验证码登录
     */
    LoginVO loginByEmailAndVarCode(String email, String varCode);

    /**
     * 邮箱+验证码登录（安全）
     * @param email 邮箱（加密）
     * @param varCode 验证码（加密）
     * @return token
     */
    LoginVO securityLoginByEmailAndVarCode(String email, String varCode);

    /**
     * 手机号码+验证码注册
     */
    LoginVO loginBySmsAndVarCode(String phone, String varCode);

    /**
     * 手机号码+验证码注册（安全）
     */
    LoginVO securityLoginBySmsAndVarCode(String phone, String varCode);

    /**
     * 跳过绑定
     */
    Map<String, Object> skipBind();

    /**
     * 安全登录
     * @param username 用户名(加密)
     * @param password 密码(加密)
     * @return token
     */
    LoginVO securityLogin(String username,String password);

    /**
     * 安全注册
     * @param username 用户名(加密)
     * @param password 密码(加密)
     * @return token
     */
    LoginVO securityRegister(String username,String password);

    /**
     * 生成密钥，并返回公钥
     * @return 公钥
     */
    String getRsaPublicKey();

    /**
     * 根据公钥获取私钥
     * @param publicKey 公钥
     * @return 私钥
     */
    String getRsaPrivateKey(String publicKey);

    /**
     * 手机号码+验证码初始化绑定
     * @param phone 手机号码
     * @param varCode 验证码
     * @return 用户信息
     */
    Map<String,Object> initBindBySmsAndVarCode(String phone, String varCode);


    /**
     * 手机号码+验证码初始化绑定（安全）
     * @param phone 手机号码
     * @param varCode 验证码
     * @return 用户信息
     */
    Map<String,Object> securityInitBindBySmsAndVarCode(String phone, String varCode);

    /**
     * 邮箱+验证码初始化绑定
     * @param email 邮箱
     * @param varCode 验证码
     * @return 用户信息
     */
    Map<String,Object> initBindByEmailAndVarCode(String email, String varCode);

    /**
     * 邮箱+验证码初始化绑定（安全）
     * @param email 邮箱
     * @param varCode 验证码
     * @return 用户信息
     */
    Map<String,Object> securityInitBindByEmailAndVarCode(String email, String varCode);

    /**
     * 设置属性值
     * @param userId 用户id
     * @param attributeCode 属性编码
     * @param value 属性值
     * @return 属性
     */
    SysUserAttribute setAttributeValue(Long userId, String attributeCode, String value);

    /**
     * 添加属性详情
     * @param userId 用户id
     * @param attributeCode 属性编码
     * @param details 属性详情
     */
    void addAttributeDetail(Long userId, String attributeCode, List<SysUserAttributeDetail> details);

    /**
     * 设置属性详情
     * @param userId 用户id
     * @param attributeCode 属性编码
     * @param details 属性详情
     */
    void setAttributeDetail(Long userId, String attributeCode, List<SysUserAttributeDetail> details);

    /**
     * 上线
     */
    void online(Long userId);

    /**
     * 离线
     */
    void offline(Long userId);

    /**
     * 用户名是否存在
     */
    Boolean isUsernameExist(String username);

    /**
     * 给用户添加角色
     */
    void addRole(Long userId, List<String> roleCodes);

    /**
     * 给用户设置角色
     */
    void setRole(Long userId, List<String> roleCodes);

    /**
     * 修改密码
     */
    void changePassword(String oldPassword, String newPassword);

    /**
     * 新建用户
     */
    SysUser create(SysUser sysUser);

    /**
     * 初始化个人信息
     */
    Map<String,Object> initSelfInfo(SysUserInitInfoDTO dto);


    PageResult getBodyData(PageParam<SysUserSearchDTO> pageParam);

    /**
     * 注册加验证码
     * @param phone 用户名
     * @param password 密码
     * @param varCode 验证码
     */
    LoginVO phoneRegisterWithVarCode(String phone, String password, String varCode);

    /**
     * 安全注册加验证码
     * @param phone 用户名（加密）
     * @param password 密码（加密）
     * @param varCode 验证码（加密）
     */
    LoginVO securityPhoneRegisterWithVarCode(String phone, String password, String varCode);

    /**
     * 生成自身的直属码
     */
    Map<String,Object> generateSelfDirectCode();

    /**
     * 生成微信号
     */
    Map<String,Object> setWechat(String wechat);


    // TODO 只针对那个dtu项目的修改与保存
    /**
     * 保存
     */
    Map<String,Object> saveOfDtu(SysUserSaveDTOOfDtu dto);

}
