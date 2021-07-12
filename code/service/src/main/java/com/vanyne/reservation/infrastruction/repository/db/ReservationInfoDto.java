package com.vanyne.reservation.infrastruction.repository.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author : Yang Jian
 * @date : 2021/7/12 0012 22:21
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReservationInfoDto {
    /**
     * 房间id
     */
    private String roomName;

    /**
     * 房间id
     */
    private String userName;

    /**
     * 预约开始时间
     */
    private Date startTime;

    /**
     * 预约结束时间
     */
    private Date endTime;

}
