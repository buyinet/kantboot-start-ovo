package com.kantboot.system.user.event;

import com.kantboot.system.user.domain.entity.SysUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 用户初始化完成的事件
 */
@Getter
@Setter
public class UserInitEvent extends ApplicationEvent {

    // 用户id
    private SysUser user;

    public UserInitEvent(Object source, SysUser user) {
        super(source);
        this.user = user;
    }



}
