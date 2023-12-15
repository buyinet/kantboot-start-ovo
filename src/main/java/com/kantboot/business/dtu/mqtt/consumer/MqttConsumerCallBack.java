package com.kantboot.business.dtu.mqtt.consumer;

import com.kantboot.business.dtu.domain.entity.BusDtuStatus;
import com.kantboot.business.dtu.service.IBusDtuService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqttConsumerCallBack implements MqttCallback {

    @Resource
    private IBusDtuService dtuService;


    @Value("${spring.mqtt.username}")
    private String username;

    @Value("${spring.mqtt.password}")
    private String password;

    @Value("${spring.mqtt.url}")
    private String hostUrl;

    @Value("${spring.mqtt.client.id}")
    private String clientId;

    @Value("${spring.mqtt.default.topic}")
    private String defaultTopic;

    /**
     * 客户端对象
     */
    private MqttClient client;

    /**
     * 在bean初始化后连接到服务器
     */
    @PostConstruct
    public void init(){
        connect();
    }

    /**
     * 客户端连接服务端
     */
    public void connect(){
        try {
            //创建MQTT客户端对象
            client = new MqttClient(hostUrl,clientId,new MemoryPersistence());
            //连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            //是否清空session，设置为false表示服务器会保留客户端的连接记录，客户端重连之后能获取到服务器在客户端断开连接期间推送的消息
            //设置为true表示每次连接到服务端都是以新的身份
            options.setCleanSession(false);
            //设置连接用户名
            options.setUserName(username);
            //设置连接密码
            options.setPassword(password.toCharArray());
            //设置超时时间，单位为秒
            options.setConnectionTimeout(100);
            //设置心跳时间 单位为秒，表示服务器每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线
            options.setKeepAliveInterval(20);
            //设置遗嘱消息的话题，若客户端和服务器之间的连接意外断开，服务器将发布客户端的遗嘱信息
            options.setWill("willTopic",(clientId + "与服务器断开连接").getBytes(),0,false);
            //设置回调
            client.setCallback(this);
            client.connect(options);
            //订阅主题
            //消息等级，和主题数组一一对应，服务端将按照指定等级给订阅了主题的客户端推送消息
            int[] qos = {1,1};
            //主题
            String[] topics = {"dtu/+/message","topic2"};
            //订阅主题
            client.subscribe(topics,qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开连接
     */
    public void disConnect(){
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    /**
     * 订阅主题
     */
    public void subscribe(String topic,int qos){
        try {
            client.subscribe(topic,qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    /**
     * 客户端断开连接的回调
     */
    @Override
    public void connectionLost(Throwable throwable) {
//        System.out.println("与服务器断开连接，可重连");
        connect();
    }

    /**
     * 消息到达的回调
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
//        System.err.println(String.format("接收消息主题 : %s",topic));

        String msg = new String(message.getPayload());
//        System.out.println(String.format("接收消息内容 : %s",msg));
//        System.out.println(String.format("接收消息retained : %b",message.isRetained()));
        BusDtuStatus deviceData = parseHexData(msg);
//        dtuService.preUpdateDtuStatus(deviceData);
        dtuService.updateDtuStatus(deviceData);

//        log.info("数据：{}",deviceData);
    }

    private static BusDtuStatus parseHexData(String msgStr) {
        String substring = msgStr.split(",")[1].substring(6);
//        log.info(substring);
        String hexData = substring.replaceAll("\\s", "");
        BusDtuStatus deviceData = new BusDtuStatus();
        //imei
        deviceData.setImei(msgStr.split(",")[0]);
        // 电流01
        deviceData.setElectricCurrent01(get32Float(extractHexSubstring(hexData, 1, 2)));

        // 电流02
        deviceData.setElectricCurrent02(get32Float(extractHexSubstring(hexData, 3, 4)));

        // 电压01
        deviceData.setVoltage01(get32Float(extractHexSubstring(hexData, 5, 6)));

        // 频率01
        deviceData.setFrequency01(get32Float(extractHexSubstring(hexData, 7, 8)));

        // 电流03
        deviceData.setElectricCurrent03(get32Float(extractHexSubstring(hexData, 9, 10)));

        // 电流04
        deviceData.setElectricCurrent04(get32Float(extractHexSubstring(hexData, 11, 12)));

        // 电压02
        deviceData.setVoltage02(get32Float(extractHexSubstring(hexData, 13, 14)));

        // 频率02
        deviceData.setFrequency02(get32Float(extractHexSubstring(hexData, 15, 16)));

        // 过电压值
        deviceData.setOverVoltageValue(get32Float(extractHexSubstring(hexData, 17, 18)));

        // 过电压时限
        deviceData.setOverVoltageTimeLimit(get16Long(extractHexSubstring(hexData, 19, 19)));

        // 过电压次数
        deviceData.setOverVoltageCount(get16Long(extractHexSubstring(hexData, 20, 20)));

        // 低电压值
        deviceData.setLowVoltageValue(get32Float(extractHexSubstring(hexData, 21, 22)));

        // 低电压时限
        deviceData.setLowVoltageTimeLimit(get16Long(extractHexSubstring(hexData, 23, 23)));

        // 低电压次数
        deviceData.setLowVoltageCount(get16Long(extractHexSubstring(hexData, 24, 24)));

        // 漏电流值
        deviceData.setLeakageCurrentValue(get32Float(extractHexSubstring(hexData, 25, 26)));

        // 漏电流时限
        deviceData.setLeakageCurrentTimeLimit(get16Long(extractHexSubstring(hexData, 27, 27)));

        // 漏电流次数
        deviceData.setLeakageCurrentCount(get16Long(extractHexSubstring(hexData, 28, 28)));

        // 过载保护
        deviceData.setOverloadProtection(get32Float(extractHexSubstring(hexData, 29, 30)));

        // 过载时限
        deviceData.setOverloadTimeLimit(get16Long(extractHexSubstring(hexData, 31, 31)));

        // 过载次数
        deviceData.setOverloadCount(get16Long(extractHexSubstring(hexData, 32, 32)));

        // 短路次数
        deviceData.setShortCircuitCount(get16Long(extractHexSubstring(hexData, 33, 33)));

        // 雷击次数
        deviceData.setLightningStrikeCount(get16Long(extractHexSubstring(hexData, 34, 34)));

        // 重合闸次数
        deviceData.setClosingOperationCount(get16Long(extractHexSubstring(hexData, 35, 35)));

        // 保存
        deviceData.setSaveCount(get16Long(extractHexSubstring(hexData, 36, 36)));

        // 过压合闸电压
        deviceData.setOverVoltageClosingVoltage(get32Float(extractHexSubstring(hexData, 37, 38)));

        // 欠压合闸电压
        deviceData.setUnderVoltageClosingVoltage(get32Float(extractHexSubstring(hexData, 39, 40)));

        // 过压状态
        deviceData.setOverVoltageStatus(get16Long(extractHexSubstring(hexData, 41, 41)));

        // 欠压状态
        deviceData.setUnderVoltageStatus(get16Long(extractHexSubstring(hexData, 42, 42)));

        // 漏电状态
        deviceData.setLeakageStatus(get16Long(extractHexSubstring(hexData, 43, 43)));

        // 过载状态
        deviceData.setOverloadStatus(get16Long(extractHexSubstring(hexData, 44, 44)));

        // 短路状态
        deviceData.setShortCircuitStatus(get16Long(extractHexSubstring(hexData, 45, 45)));

        // 重合闸动作次数
        deviceData.setNumberOfreclosingActions(get16Long(extractHexSubstring(hexData, 46, 46)));

        // 重合闸剩余时间
        deviceData.setClosingOperationRemainingTime(get32Float(extractHexSubstring(hexData, 47, 48)));

        // 重合闸故障复归时间
        deviceData.setClosingOperationFaultRecoveryTime(get32Float(extractHexSubstring(hexData, 49, 50)));

        // 输出状态指示
        deviceData.setOutputStatusIndication(get16Long(extractHexSubstring(hexData, 51, 51)));

        // 过流设定重合闸次数
        deviceData.setOvercurrentSettingClosingCount(get16Long(extractHexSubstring(hexData, 52, 52)));

        // 过载重合闸动作次数
        deviceData.setOverloadClosingOperationCount(get16Long(extractHexSubstring(hexData, 53, 53)));

        // 过载动作合闸倒计时
        deviceData.setOverloadActionClosingCountdown(get32Float(extractHexSubstring(hexData, 54, 55)));

        // 过载重合闸计次复归时间
        deviceData.setOverloadClosingCountResetTime(get32Float(extractHexSubstring(hexData, 56, 57)));

        // 总复归
        deviceData.setTotalReturn(get16Long(extractHexSubstring(hexData, 58, 58)));
        // 雷击电
        deviceData.setLightningStrikeVoltageSetting(get32Float(extractHexSubstring(hexData, 59, 60)));

        // 雷击单次屏蔽时间
        deviceData.setLightningStrikeSingleTimeShieldTime(get16Long(extractHexSubstring(hexData, 61, 61)));


        return deviceData;
    }
    private static String extractHexSubstring(String hexData, int start, int end) {
        // 根据起始位置和结束位置提取十六进制子串
        int startIndex = (start - 1) * 4;
        int endIndex = Math.min(end * 4, hexData.length());
        return hexData.substring(startIndex, endIndex);
    }
    //解析但单精度16进制
    private static float get32Float(String hexValue){

        // 将16进制字符串转换为整数
        int intValue = Integer.parseInt(hexValue, 16);

        // 使用Float.intBitsToFloat将整数表示的单精度浮点数转换为float
        float floatValue = Float.intBitsToFloat(intValue);


        return floatValue;

    }
    //解析但16为无符号
    private static long get16Long(String hexValue){
        // 将16进制字符串转换为长整数
        long longValue = Long.parseLong(hexValue, 16);
        return longValue;
    }


    /**
     * 消息发布成功的回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

}

