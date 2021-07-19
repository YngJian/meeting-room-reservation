package com.vanyne.reservation.infrastruction.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vanyne.reservation.infrastruction.repository.db.dto.ReservationInfoDto;
import com.vanyne.reservation.infrastruction.repository.db.dto.ReservationInfoQo;
import com.vanyne.reservation.infrastruction.repository.db.entity.ReservationInfoEntity;

/**
 * @author : Yang Jian
 * @date : 2021/7/12 0006 22:29
 */
public interface ReservationInfoRepository {
    /**
     * 分页查询
     *
     * @param page              page
     * @param reservationInfoQo r
     * @return list
     */
    Page<ReservationInfoDto> getReservationsByPage(Page<ReservationInfoEntity> page, ReservationInfoQo reservationInfoQo);
}