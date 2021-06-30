package com.vanyne.reservation.infrastruction.repository.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vanyne.reservation.infrastruction.repository.db.entity.ReservationInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationInfoMapper extends BaseMapper<ReservationInfo> {
}