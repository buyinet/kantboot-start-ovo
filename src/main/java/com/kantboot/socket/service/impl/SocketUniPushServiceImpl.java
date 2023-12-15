package com.kantboot.socket.service.impl;

import com.kantboot.socket.domain.entity.SocketUniPushBind;
import com.kantboot.socket.repository.SocketUniPushBindRepository;
import com.kantboot.socket.service.ISocketUniPushService;
import com.kantboot.system.user.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SocketUniPushServiceImpl implements ISocketUniPushService {

    @Resource
    private SocketUniPushBindRepository repository;

    @Resource
    private ISysUserService userService;

    @Override
    public SocketUniPushBind bind(String cid) {
        Long selfId = userService.getSelfId();
        SocketUniPushBind byUserIdAndCid = repository.findByUserIdAndCid(selfId,cid);
        if (byUserIdAndCid != null) {
            byUserIdAndCid.setUserId(selfId);
            byUserIdAndCid.setCid(cid);
            return repository.save(byUserIdAndCid);
        }
        return repository.save(new SocketUniPushBind().setUserId(selfId).setCid(cid));
    }
}
