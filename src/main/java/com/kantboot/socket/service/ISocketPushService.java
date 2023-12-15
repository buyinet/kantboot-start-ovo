package com.kantboot.socket.service;

import com.kantboot.socket.domain.dto.SocketPushMessageDTO;
import com.kantboot.socket.domain.entity.SocketPushMessage;

public interface ISocketPushService {

    SocketPushMessage push(SocketPushMessageDTO message);

    /**
     * uniPush发送消息
     */
    SocketPushMessage pushOfUniPush(SocketPushMessageDTO message);

}
