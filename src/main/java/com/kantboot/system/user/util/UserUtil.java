package com.kantboot.system.user.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.kantboot.system.user.domain.entity.*;
import lombok.extern.slf4j.Slf4j;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class UserUtil {

    /**
     * 转对象之后的操作
     */
    private static Map<String,Object> afterToEntity(SysUser entity,Map<String,Object> result) {

        entity.setPassword(null);
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

        // 添加余额
        List<SysUserBalance> balanceList = entity.getBalanceList();
        if(balanceList==null){
            balanceList = new ArrayList<>();
        }
        Map<String, Double> balanceMap = new HashMap<>();
        for (SysUserBalance sysUserBalance : balanceList) {
            balanceMap.put(sysUserBalance.getBalanceCode(), sysUserBalance.getNumber());
            result.put("balance" + StrUtil.upperFirst(sysUserBalance.getBalanceCode()), sysUserBalance.getNumber());
        }
        result.put("balance", balanceMap);

        try{
            result.put("gmtCreate", entity.getGmtCreate().getTime());
            result.put("gmtModified", entity.getGmtModified().getTime());
        }catch (Exception e){
            result.put("gmtCreate", null);
            result.put("gmtModified", null);
        }
        return result;
    }


    /**
     * 将对象转换为map
     * @param entity 实体对象
     * @return map
     */
    public static Map<String, Object> entityToMap(SysUser entity) {
        // 如果没有在线信息，就添加一个

        if(entity.getOnline()==null){
            entity.setOnline(
                    new SysUserOnline()
                            .setOnline(false)
                            .setUserId(entity.getId())
                            .setOnlineStatusCode(SysUserOnline.ONLINE_STATUS_CODE_OFFLINE)
            );
        }
        // 开始时间
        long startTime = System.currentTimeMillis();
        Map<String, Object> result = new HashMap<>();
        result.put("id", entity.getId());
        result.put("isTemporary", entity.getIsTemporary());
        result.put("isInit", entity.getIsInit());
        result.put("username", entity.getUsername());
        result.put("phone", entity.getPhone());
        result.put("email", entity.getEmail());
        result.put("genderCode", entity.getGenderCode());
        result.put("password", entity.getPassword());
        result.put("nickname", entity.getNickname());
        result.put("fileIdOfAvatar", entity.getFileIdOfAvatar());
        result.put("introduction", entity.getIntroduction());
        result.put("gmtBirthday", entity.getGmtBirthday());
        result.put("sexualOrientation", entity.getSexualOrientation());
        result.put("sadomasochismCode", entity.getSadomasochismCode());
        result.put("realName", entity.getRealName());
        result.put("directCode", entity.getDirectCode());
        result.put("inviteCode", entity.getInviteCode());
        result.put("inviteUserId", entity.getInviteUserId());
        result.put("wechat", entity.getWechat());
        result.put("inviteUser", entity.getInviteUser());
        result.put("invite", entity.getInvite());
        result.put("inviteCount", entity.getInviteCount());
        result.put("inviteCountIndirect", entity.getInviteCountIndirect());
        result.put("balanceList", entity.getBalanceList());
        result.put("orgId", entity.getOrgId());
        result.put("roleCode", entity.getRoleCode());
        result.put("roleList", entity.getRoleList());
        result.put("roleListHas", entity.getRoleListHas());
        result.put("attributeList", entity.getAttributeList());
        result.put("online", entity.getOnline());
        result.put("relOrgList", entity.getRelOrgList());
        result.put("orgList", entity.getOrgList());


        afterToEntity(entity,result);

        // 结束时间
        long endTime = System.currentTimeMillis();
        log.info("entityToMap耗时：{}ms", endTime - startTime);
        return result;
    }


    /**
     * 隐藏敏感的用户信息
     */
    private Map<String, Object> hideMap(SysUser entity) {
        SysUser newUser = BeanUtil.copyProperties(entity, SysUser.class);
        // 隐藏余额信息
        newUser.setBalanceList(new ArrayList<>());
        List<SysUserRole> newRoleList = new ArrayList<>();
        List<SysUserRole> roleList = entity.getRoleList();
        for (SysUserRole role : roleList) {
            // 隐藏不可见的角色
            if(role.getVisible()!=null&&!role.getVisible()){
                newRoleList.add(role);
            }
        }
        newUser.setRoleList(newRoleList);

        Map<String, Object> map = UserUtil.entityToMap(newUser);
        map.remove("username");
        map.remove("password");
        map.remove("balanceList");
        map.remove("balance");
        return map;
    }

    /**
     * 批量将对象转换为map
     */
    public static List<Map<String, Object>> entityListToMap(List<SysUser> entityList) {

        List<Map<String, Object>> result = new ArrayList<>();
        for (SysUser entity : entityList) {
            result.add(UserUtil.entityToMap(entity));
        }
        return result;

    }



}
