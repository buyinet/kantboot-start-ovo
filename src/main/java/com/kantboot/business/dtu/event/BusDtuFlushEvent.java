package com.kantboot.business.dtu.event;

import com.kantboot.business.dtu.domain.entity.BusDtu;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class BusDtuFlushEvent extends ApplicationEvent {

    private BusDtu dtu;

    public BusDtuFlushEvent(Object source, BusDtu dtu) {
        super(source);
        this.dtu = dtu;
    }

}
