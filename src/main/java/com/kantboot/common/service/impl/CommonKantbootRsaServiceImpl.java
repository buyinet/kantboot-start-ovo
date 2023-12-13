package com.kantboot.common.service.impl;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.kantboot.common.service.ICommonKantbootRsaService;
import com.kantboot.util.common.exception.BaseException;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CommonKantbootRsaServiceImpl implements ICommonKantbootRsaService {

    @Resource
    private RedisUtil redisUtil;

    /**
     * 根据公钥获取私钥
     */
    @Override
    public String getPrivateKeyByPublicKey(String publicKey) {
        String result = redisUtil.get("rsa::getPrivateKeyByPublicKey:" + publicKey);
        if (StrUtil.isBlank(result)) {
            // 未找到私钥
            throw BaseException.of("privateKeyNotFound", "未找到私钥");
        }
        return result;
    }

    /**
     * 生成公钥
     */
    @Override
    public String generatePublicKey() {
        RSA rsa = new RSA();
        String publicKey = rsa.getPublicKeyBase64();
        String privateKey = rsa.getPrivateKeyBase64();
        redisUtil.setEx("rsa::getPrivateKeyByPublicKey:" + publicKey, privateKey, 30, TimeUnit.MINUTES);
        return publicKey;
    }

    /**
     * 加密
     */
    @Override
    public String encrypt(String content, String publicKey) {
        content=content.trim();
        RSA rsa = new RSA(getPrivateKeyByPublicKey(publicKey), publicKey);
        byte[] encrypt = rsa.encrypt(content, KeyType.PrivateKey);
        return publicKey + "&&" + Base64Encoder.encode(encrypt);
    }

    /**
     * 解密
     */
    @Override
    public String decrypt(String content) {
        content=content.trim();
        String[] split = content.split("&&");
        if (split.length != 2) {
            throw BaseException.of("decryptError", "解密失败");
        }
        String publicKey = split[0];
        String privateKey = redisUtil.get("rsa::getPrivateKeyByPublicKey:" + publicKey);
        RSA rsa = new RSA(privateKey, publicKey);
        return rsa.decryptStr(split[1], KeyType.PrivateKey);
    }

}
