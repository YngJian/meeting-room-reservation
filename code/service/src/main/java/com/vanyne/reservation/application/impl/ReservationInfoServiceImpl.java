package com.vanyne.reservation.application.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanyne.reservation.application.ReservationInfoService;
import com.vanyne.reservation.infrastruction.repository.db.entity.ReservationInfoEntity;
import com.vanyne.reservation.infrastruction.repository.db.mapper.ReservationInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class ReservationInfoServiceImpl extends ServiceImpl<ReservationInfoMapper, ReservationInfoEntity> implements ReservationInfoService {

}
