package com.vanyne.reservation.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanyne.reservation.application.impl.MeetRoomInfoService;
import com.vanyne.reservation.infrastruction.repository.db.entity.MeetRoomInfoEntity;
import com.vanyne.reservation.infrastruction.repository.db.mapper.MeetRoomInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class MeetRoomInfoServiceImpl extends ServiceImpl<MeetRoomInfoMapper, MeetRoomInfoEntity> implements MeetRoomInfoService {

}
