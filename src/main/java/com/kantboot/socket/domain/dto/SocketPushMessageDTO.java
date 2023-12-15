package com.kantboot.socket.domain.dto;

import com.kantboot.socket.domain.entity.SocketPushMessagePayload;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SocketPushMessageDTO implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * cid
     */
    private String cid;

    /**
     * 是否强制通知
     */
    private Boolean forceNotification;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息ttl
     */
    private Long ttl;

    /**
     * 消息payload
     */
    private SocketPushMessagePayload payload;

    /**
     * 消息中间件
     */
    private List<String> brokerCodeList;

}
