package com.kantboot.socket.domain.entity;

import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Unipush推送消息表
 */
@Table(name="socket_uni_push_message")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SocketPushMessage implements Serializable {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * cid
     */
    @Column(name = "cid")
    private String cid;


    /**
     * 是否强制通知
     */
    @Column(name = "force_notification")
    private Boolean forceNotification;

    /**
     * 消息标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 消息内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 消息ttl
     */
    @Column(name = "ttl")
    private Long ttl;

    /**
     * payload的id
     */
    @Column(name = "payload_id")
    private Long payloadId;

    /**
     * 消息payload
     */
    @OneToOne
    @JoinColumn(name = "payload_id",referencedColumnName = "id",insertable = false,updatable = false)
    private SocketPushMessagePayload payload;

    /**
     * 消息中间件
     */
    @OneToMany
    @JoinColumn(name = "message_id",referencedColumnName = "id",insertable = false,updatable = false)
    private List<SocketPushMessageBroker> broker;


    /**
     * 创建时间
     */
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "gmt_modified")
    private Date gmtModified;

}
