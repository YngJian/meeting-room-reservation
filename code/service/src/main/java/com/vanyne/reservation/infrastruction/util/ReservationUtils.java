package com.vanyne.reservation.infrastruction.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.vanyne.reservation.infrastruction.repository.db.ReservationInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @author : Yang Jian
 * @date : 2021/7/12 0012 22:40
 */
@Slf4j
public class ReservationUtils {
    private ReservationUtils() {
    }

    public static boolean isFormat(String startTime, ReservationInfoDto reservationInfoDto, String msg) {
        if (!StringUtils.isEmpty(startTime)) {
            try {
                DateTime dateTime = DateUtil.parse(startTime, DatePattern.NORM_DATETIME_PATTERN);
                reservationInfoDto.setStartTime(dateTime);
            } catch (Exception e) {
                log.error(msg, e);
                return true;
            }
        }
        return false;
    }

}
