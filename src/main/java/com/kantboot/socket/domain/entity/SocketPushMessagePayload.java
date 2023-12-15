package com.kantboot.socket.domain.entity;

import com.alibaba.fastjson2.JSON;
import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Table(name="socket_uni_push_message_payload")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SocketPushMessagePayload implements Serializable {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * emit
     */
    @Column(name = "emit")
    private String emit;

    /**
     * 消息内容
     */
    @Column(name = "data_json_str")
    private String dataJsonStr;

    /**
     * 消息内容
     */
    @Transient
    private Object data;

    public void setData(Object data) {
        if(data == null){
            return;
        }
        this.data = data;
        this.dataJsonStr = JSON.toJSONString(data);
    }
}
