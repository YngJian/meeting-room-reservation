package com.vanyne.reservation.infrastruction.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author : Yang Jian
 * @date : 2021/7/12 0012 22:40
 */
@Slf4j
public class DateUtils {
    private DateUtils() {
    }

    public static Long parseTimeToLong(Date date) {
        return date == null ? null : date.getTime();
    }
}
