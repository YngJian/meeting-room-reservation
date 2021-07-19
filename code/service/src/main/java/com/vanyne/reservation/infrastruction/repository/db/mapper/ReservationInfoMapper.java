package com.vanyne.reservation.infrastruction.repository.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vanyne.reservation.infrastruction.repository.db.dto.ReservationInfoDto;
import com.vanyne.reservation.infrastruction.repository.db.dto.ReservationInfoQo;
import com.vanyne.reservation.infrastruction.repository.db.entity.ReservationInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReservationInfoMapper extends BaseMapper<ReservationInfoEntity> {

    /**
     * 分页查询
     *
     * @param page              page
     * @param reservationInfoQo r
     * @return list
     */
    Page<ReservationInfoDto> getReservationsByPage(Page<ReservationInfoEntity> page,
                                                   @Param("reservationInfoQo") ReservationInfoQo reservationInfoQo);
}