package com.vanyne.reservation.infrastruction.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    /**
     * 是否有被预约
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param roomId    user id
     * @return int
     */
    @Override
    public int IsAppointment(String startTime, String endTime, String roomId) {
        QueryWrapper<ReservationInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ReservationInfoEntity.COL_ROOM_ID, roomId);
        queryWrapper.gt(ReservationInfoEntity.COL_END_TIME, startTime);
        queryWrapper.lt(ReservationInfoEntity.COL_START_TIME, endTime);
        return reservationInfoMapper.selectCount(queryWrapper);
    }
}