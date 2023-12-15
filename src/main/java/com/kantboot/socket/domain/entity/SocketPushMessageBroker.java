package com.kantboot.socket.domain.entity;

import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

/**
 * 发送消息的中间件
 */
@Table(name="socket_uni_push_message_broker")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SocketPushMessageBroker implements Serializable {

    /**
     * 发送消息的中间件
     * uniPush
     */
    public static final String CODE_UNI_PUSH = "uniPush";

    /**
     * 发送消息的中间件
     * mqtt
     */
    public static final String CODE_MQTT = "mqtt";

    /**
     * 发送消息的中间件
     * websocket
     */
    public static final String CODE_WEBSOCKET = "websocket";

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 中间件编码
     */
    @Column(name = "t_code")
    private String code;

    /**
     * 绑定消息
     */
    @Column(name = "message_id")
    private Long messageId;

}
