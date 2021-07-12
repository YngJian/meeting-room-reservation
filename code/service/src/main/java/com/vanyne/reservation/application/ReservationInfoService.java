package com.vanyne.reservation.application;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vanyne.reservation.infrastruction.repository.db.ReservationInfoDto;
import com.vanyne.reservation.infrastruction.repository.db.entity.ReservationInfoEntity;
import com.vayne.model.model.ListReservationInfoRep;

public interface ReservationInfoService extends IService<ReservationInfoEntity> {

    /**
     * 分页查询会议室预约信息
     *
     * @param token              token
     * @param reservationInfoDto reservationInfoDto
     * @param pageNum            页码
     * @param pageSize           条数
     * @return list
     */
    ListReservationInfoRep listReservationInfo(String token, ReservationInfoDto reservationInfoDto,
                                               Integer pageNum, Integer pageSize);
}
