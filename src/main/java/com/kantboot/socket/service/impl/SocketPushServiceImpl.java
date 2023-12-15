package com.kantboot.socket.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.kantboot.socket.domain.dto.SocketPushMessageDTO;
import com.kantboot.socket.domain.entity.SocketPushMessage;
import com.kantboot.socket.domain.entity.SocketPushMessageBroker;
import com.kantboot.socket.domain.entity.SocketUniPushBind;
import com.kantboot.socket.repository.SocketUniPushBindRepository;
import com.kantboot.socket.service.ISocketPushService;
import com.kantboot.system.setting.service.ISysSettingService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SocketPushServiceImpl implements ISocketPushService {

    @Resource
    private ISysSettingService service;

    @Resource
    private SocketUniPushBindRepository repository;


    @Override
    public SocketPushMessage push(SocketPushMessageDTO message) {
        return null;
    }

    @SneakyThrows
    @Override
    public SocketPushMessage pushOfUniPush(SocketPushMessageDTO message) {
        SocketPushMessage socketPushMessage = BeanUtil.toBean(message, SocketPushMessage.class);
        List<SocketPushMessageBroker> broker = new ArrayList<>();
        broker.add(new SocketPushMessageBroker().setCode(SocketPushMessageBroker.CODE_UNI_PUSH));
        socketPushMessage.setBroker(broker);
        List<SocketUniPushBind> byUserId = repository.findByUserId(message.getUserId());
        String url = "https://fc-mp-c65b0fed-3265-4ff1-bdf1-0071142ed68b.next.bspapp.com/push";

        for (SocketUniPushBind socketUniPushBind : byUserId) {
            socketPushMessage.setCid(socketUniPushBind.getCid());
            // 使用okhttp发送application/json格式的post请求 数据是socketPushMessage
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(socketPushMessage));
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            log.info(JSON.toJSONString(socketPushMessage) + "-" + response.body().string());

        }

        return socketPushMessage;
    }
}
