package com.vanyne.reservation.application;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vanyne.reservation.infrastruction.repository.db.dto.ReservationInfoQo;
import com.vanyne.reservation.infrastruction.repository.db.entity.ReservationInfoEntity;
import com.vayne.model.model.CreateReservationInfoReq;
import com.vayne.model.model.ListReservationInfoRep;
import com.vayne.model.model.ReservationInfoRep;
import com.vayne.model.model.UpdateReservationInfoReq;

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

    /**
     * 创建会议室预约信息
     *
     * @param createReservationInfoReq createReservationInfoReq
     * @param token                    token
     * @return ReservationInfoRep
     */
    ReservationInfoRep createReservationInfo(CreateReservationInfoReq createReservationInfoReq, String token);

    /**
     * 删除会议室预约信息信息
     *
     * @param id    id
     * @param token token
     * @return disableReservationInfoRep
     */
    ReservationInfoRep deleteReservationInfo(Integer id, String token);

    /**
     * 修改会议室预约信息信息
     *
     * @param id                       id
     * @param token                    token
     * @param updateReservationInfoReq updateReservationInfoReq
     * @return disableReservationInfoRep
     */
    ReservationInfoRep updateReservationInfo(Integer id, String token, UpdateReservationInfoReq updateReservationInfoReq);
}
