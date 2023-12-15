package com.kantboot.socket.controller;

import com.kantboot.socket.domain.entity.SocketUniPushBind;
import com.kantboot.socket.service.ISocketUniPushService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/socket-web/uniPush")
public class SocketUniPushController {

    @Resource
    private ISocketUniPushService service;

    @RequestMapping("/bind")
    public RestResult<SocketUniPushBind> bind(@RequestParam("cid") String cid){
        return RestResult.success(service.bind(cid),"bindSuccess","绑定成功");
    }


}
