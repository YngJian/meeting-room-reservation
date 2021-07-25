package com.vanyne.reservation.infrastruction.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

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

    /**
     * 比较时间
     *
     * @param startTime startTime
     * @param endTime   endTime
     * @return true 开始时间大于结束时间，false 开始时间小于结束时间
     */
    public static boolean compareTime(Date startTime, Date endTime) {
        if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
            return false;
        }
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)) {
            return startTime.after(endTime);
        }
        return false;
    }

    /**
     * 时间格式是否正确
     *
     * @param date date
     * @return true 正确，false 不正确
     */
    public static Optional<Date> parse(String date) {
        if (!StringUtils.isEmpty(date)) {
            try {
                return Optional.of(DateUtil.parse(date, DatePattern.NORM_DATETIME_PATTERN));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    /**
     * local Date time To Date
     *
     * @param localDateTime localDateTime
     * @return date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * Date To Local Date time
     *
     * @param date date
     * @return LocalDate
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        //系统默认的时区
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zoneId);
    }
}
