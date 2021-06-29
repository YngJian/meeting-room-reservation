package com.vanyne.reservation.application;

import com.vanyne.reservation.application.impl.ReservationInfoService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanyne.reservation.repository.db.mapper.ReservationInfoMapper;
import com.vanyne.reservation.repository.db.entity.ReservationInfo;

@Service
public class ReservationInfoServiceImpl extends ServiceImpl<ReservationInfoMapper, ReservationInfo> implements ReservationInfoService {

}
