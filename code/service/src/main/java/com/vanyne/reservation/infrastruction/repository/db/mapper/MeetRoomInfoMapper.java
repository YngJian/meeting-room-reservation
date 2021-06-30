package com.vanyne.reservation.infrastruction.repository.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vanyne.reservation.infrastruction.repository.db.entity.MeetRoomInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MeetRoomInfoMapper extends BaseMapper<MeetRoomInfo> {
}