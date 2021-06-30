package com.vanyne.reservation.application;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanyne.reservation.application.impl.ReservationInfoService;
import com.vanyne.reservation.infrastruction.repository.db.entity.ReservationInfo;
import com.vanyne.reservation.infrastruction.repository.db.mapper.ReservationInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class ReservationInfoServiceImpl extends ServiceImpl<ReservationInfoMapper, ReservationInfo> implements ReservationInfoService {

}
