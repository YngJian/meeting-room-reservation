package com.vanyne.reservation.infrastruction.util;

import java.util.UUID;

/**
 * @author : Yang Jian
 * @date : 2021/7/6 0006 22:43
 */
public class CommonUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
