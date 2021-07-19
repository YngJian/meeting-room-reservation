package com.vanyne.reservation.infrastruction.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.vanyne.reservation.infrastruction.repository.db.dto.ReservationInfoQo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author : Yang Jian
 * @date : 2021/7/12 0012 22:40
 */
@Slf4j
public class ReservationUtils {
    private ReservationUtils() {
    }

    public static boolean isFormat(String startTime, ReservationInfoQo reservationInfoQo, String msg) {
        if (!StringUtils.isEmpty(startTime)) {
            try {
                DateTime dateTime = DateUtil.parse(startTime, DatePattern.NORM_DATETIME_PATTERN);
                reservationInfoQo.setStartTime(dateTime);
            } catch (Exception e) {
                log.error(msg, e);
                return true;
            }
        }
        return false;
    }

    public static boolean compareTime(Date startTime, Date endTime) {
        if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
            return false;
        }
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
            return startTime.after(endTime);
        }
        return false;
    }

}
