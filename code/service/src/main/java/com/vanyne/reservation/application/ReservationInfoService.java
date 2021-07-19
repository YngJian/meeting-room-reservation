package com.vanyne.reservation.application;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vanyne.reservation.infrastruction.repository.db.dto.ReservationInfoQo;
import com.vanyne.reservation.infrastruction.repository.db.entity.ReservationInfoEntity;
import com.vayne.model.model.ListReservationInfoRep;

public interface ReservationInfoService extends IService<ReservationInfoEntity> {

    /**
     * 分页查询会议室预约信息
     *
     * @param reservationInfoQo reservationInfoDto
     * @param pageNum           页码
     * @param pageSize          条数
     * @return list
     */
    ListReservationInfoRep listReservationInfo(ReservationInfoQo reservationInfoQo,
                                               Integer pageNum, Integer pageSize);
}
