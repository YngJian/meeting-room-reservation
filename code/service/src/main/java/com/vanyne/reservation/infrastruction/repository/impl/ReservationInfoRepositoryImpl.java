package com.vanyne.reservation.infrastruction.repository.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vanyne.reservation.infrastruction.repository.ReservationInfoRepository;
import com.vanyne.reservation.infrastruction.repository.db.dto.ReservationInfoDto;
import com.vanyne.reservation.infrastruction.repository.db.dto.ReservationInfoQo;
import com.vanyne.reservation.infrastruction.repository.db.entity.ReservationInfoEntity;
import com.vanyne.reservation.infrastruction.repository.db.mapper.ReservationInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author : Yang Jian
 * @date : 2021/7/12 0006 22:29
 */
@Repository
public class ReservationInfoRepositoryImpl implements ReservationInfoRepository {
    @Autowired
    private ReservationInfoMapper reservationInfoMapper;

    /**
     * 分页查询
     *
     * @param page              page
     * @param reservationInfoQo r
     * @return list
     */
    @Override
    public Page<ReservationInfoDto> getReservationsByPage(Page<ReservationInfoEntity> page, ReservationInfoQo reservationInfoQo) {
        return reservationInfoMapper.getReservationsByPage(page, reservationInfoQo);
    }
}