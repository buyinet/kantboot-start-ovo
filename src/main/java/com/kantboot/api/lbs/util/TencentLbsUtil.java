package com.kantboot.api.lbs.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.api.lbs.domain.vo.LbsVO;
import com.kantboot.util.common.exception.BaseException;
import org.yaml.snakeyaml.util.UriEncoder;

public class TencentLbsUtil {

    /**
     * 根据ip定位
     */
    public static LbsVO byIp(String ip, String key) {
        ip = UriEncoder.encode(ip);
        key = UriEncoder.encode(key);
        String jsonStr = HttpUtil.get("https://apis.map.qq.com/ws/location/v1/ip?ip=" + ip + "&key=" + key);
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        if (190 == jsonObject.getInteger("status")) {
            // 无效的key
            throw BaseException.of("invalidKey","无效的key");
        }
        JSONObject result = jsonObject.getJSONObject("result");
        if (null != result) {
            LbsVO lbsVO = new LbsVO();
            lbsVO.setIp(ip);
            lbsVO.setLat(result.getJSONObject("location").getString("lat"));
            lbsVO.setLng(result.getJSONObject("location").getString("lng"));
            lbsVO.setCountry(result.getJSONObject("ad_info").getString("nation"));
            lbsVO.setProvince(result.getJSONObject("ad_info").getString("province"));
            lbsVO.setCity(result.getJSONObject("ad_info").getString("city"));
            lbsVO.setDistrict(result.getJSONObject("ad_info").getString("district"));
            lbsVO.setStreet(result.getJSONObject("ad_info").getString("street"));
            lbsVO.setAdcode(result.getJSONObject("ad_info").getString("adcode"));
            return lbsVO;
        }

        throw BaseException.of("invalidIp","无效的ip");
    }

    /**
     * 根据经纬度定位
     */
    public static LbsVO byLngAndLat(String lng, String lat, String key) {
        lat = UriEncoder.encode(lat);
        lng = UriEncoder.encode(lng);
        key = UriEncoder.encode(key);
        String jsonStr = HttpUtil.get("https://apis.map.qq.com/ws/geocoder/v1/?location=" + lat + "," + lng + "&key=" + key);
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        if (190 == jsonObject.getInteger("status")) {
            // 无效的key
            throw BaseException.of("invalidKey", "无效的key");
        }
        JSONObject result = jsonObject.getJSONObject("result");
        if (null != result) {
            LbsVO lbsVO = new LbsVO();
            lbsVO.setLat(lat);
            lbsVO.setLng(lng);
            lbsVO.setCountry(result.getJSONObject("ad_info").getString("nation"));
            lbsVO.setProvince(result.getJSONObject("ad_info").getString("province"));
            lbsVO.setCity(result.getJSONObject("ad_info").getString("city"));
            lbsVO.setDistrict(result.getJSONObject("ad_info").getString("district"));
            lbsVO.setStreet(result.getJSONObject("ad_info").getString("street"));
            lbsVO.setAdcode(result.getJSONObject("ad_info").getString("adcode"));
            return lbsVO;
        }

        throw BaseException.of("invalidLatAndLng", "无效的经纬度");
    }



}
