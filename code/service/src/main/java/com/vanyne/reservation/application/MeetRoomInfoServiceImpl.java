package com.vanyne.reservation.application;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanyne.reservation.repository.db.mapper.MeetRoomInfoMapper;
import com.vanyne.reservation.repository.db.entity.MeetRoomInfo;
import com.vanyne.reservation.application.impl.MeetRoomInfoService;

@Service
public class MeetRoomInfoServiceImpl extends ServiceImpl<MeetRoomInfoMapper, MeetRoomInfo> implements MeetRoomInfoService {

}
