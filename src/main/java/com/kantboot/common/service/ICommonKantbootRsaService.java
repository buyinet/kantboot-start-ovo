package com.kantboot.common.service;

public interface ICommonKantbootRsaService {

    /**
     * 根据公钥获取私钥
     */
    String getPrivateKeyByPublicKey(String publicKey);

    /**
     * 生成公钥
     */
    String generatePublicKey();

    /**
     * 加密
     */
    String encrypt(String content, String publicKey);

    /**
     * 解密
     */
    String decrypt(String content);

}
