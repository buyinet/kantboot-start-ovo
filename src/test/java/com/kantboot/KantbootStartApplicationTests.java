package com.kantboot;

import com.alibaba.fastjson2.JSON;
import com.kantboot.system.user.dao.repository.SysUserInviteRepository;
import com.kantboot.system.user.dao.repository.SysUserRepository;
import com.kantboot.system.user.domain.entity.SysUser;
import com.kantboot.system.user.domain.entity.SysUserInvite;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class KantbootStartApplicationTests {

    @Resource
    private SysUserInviteRepository repository;

    @Resource
    private SysUserRepository userRepository;

    @Test
    void contextLoads() {
        SysUserInvite byUserId = repository.findByUserId(492103635865605L);
        System.err.println(JSON.toJSONString(byUserId));
        Optional<SysUser> byId = userRepository.findById(492103635865605L);
        System.err.println(JSON.toJSONString(byId.get()));
    }

}
