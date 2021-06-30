package com.vanyne.reservation.infrastruction.util;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;

/**
 * @author : Yang Jian
 * @date : 2021/6/30 0030 23:17
 */
public class AesUtils {
    public static byte[] decode(String key, String content) {
        return SecureUtil.aes(key.getBytes()).decrypt(HexUtil.decodeHex(content));
    }

    public static String encode(String key, String content) {
        byte[] encrypt = SecureUtil.aes(key.getBytes()).encrypt(content);
        return HexUtil.encodeHexStr(encrypt);
    }

    public static void main(String[] args) {
        String string = encode("vaynevaynevaynev", "123456");
        System.out.println(string);
    }

}
