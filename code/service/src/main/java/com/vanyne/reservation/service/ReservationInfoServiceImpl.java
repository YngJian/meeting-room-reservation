package com.vanyne.reservation.service;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanyne.reservation.repository.db.mapper.ReservationInfoMapper;
import com.vanyne.reservation.repository.db.entity.ReservationInfo;
import com.vanyne.reservation.service.impl.ReservationInfoService;
@Service
public class ReservationInfoServiceImpl extends ServiceImpl<ReservationInfoMapper, ReservationInfo> implements ReservationInfoService{

}
