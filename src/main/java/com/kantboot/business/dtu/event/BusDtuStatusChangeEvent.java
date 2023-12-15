package com.kantboot.business.dtu.event;

import com.kantboot.business.dtu.domain.entity.BusDtuStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class BusDtuStatusChangeEvent extends ApplicationEvent {

    private BusDtuStatus status;

    public BusDtuStatusChangeEvent(Object source,BusDtuStatus status) {
        super(source);
        this.status = status;
    }

}
