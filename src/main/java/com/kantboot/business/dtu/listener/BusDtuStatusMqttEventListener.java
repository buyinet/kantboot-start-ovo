package com.kantboot.business.dtu.listener;

import com.kantboot.business.dtu.event.BusDtuStatusMqttEvent;
import com.kantboot.business.dtu.service.IBusDtuService;
import jakarta.annotation.Resource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BusDtuStatusMqttEventListener {

    @Resource
    private IBusDtuService dtuService;


    @EventListener
    public void onApplicationEvent(BusDtuStatusMqttEvent event) {
        dtuService.updateDtuStatus(event.getStatus());
        System.out.println(event.getStatus());
    }

}
