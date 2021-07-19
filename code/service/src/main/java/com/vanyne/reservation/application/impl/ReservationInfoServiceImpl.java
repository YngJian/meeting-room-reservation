package com.vanyne.reservation.application.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanyne.reservation.application.ReservationInfoService;
import com.vanyne.reservation.domain.enums.CommonResult;
import com.vanyne.reservation.infrastruction.repository.ReservationInfoRepository;
import com.vanyne.reservation.infrastruction.repository.db.dto.ReservationInfoDto;
import com.vanyne.reservation.infrastruction.repository.db.dto.ReservationInfoQo;
import com.vanyne.reservation.infrastruction.repository.db.entity.ReservationInfoEntity;
import com.vanyne.reservation.infrastruction.repository.db.mapper.ReservationInfoMapper;
import com.vanyne.reservation.infrastruction.util.DateUtils;
import com.vayne.model.model.ListReservationInfoRep;
import com.vayne.model.model.ReservationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReservationInfoServiceImpl extends ServiceImpl<ReservationInfoMapper, ReservationInfoEntity> implements ReservationInfoService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ReservationInfoRepository reservationInfoRepository;

    /**
     * 分页查询会议室预约信息
     *
     * @param reservationInfoQo reservationInfoDto
     * @param pageNum           页码
     * @param pageSize          条数
     * @return list
     */
    @Override
    public ListReservationInfoRep listReservationInfo(ReservationInfoQo reservationInfoQo,
                                                      Integer pageNum, Integer pageSize) {
        // 分页查询数据库
        Page<ReservationInfoEntity> page = new Page<>(pageNum, pageSize);
        Page<ReservationInfoDto> reservations = reservationInfoRepository.getReservationsByPage(page, reservationInfoQo);

        List<ReservationInfoDto> records = reservations.getRecords();
        List<ReservationInfo> reservationInfoList = new ArrayList<>(records.size());
        records.forEach(reservationInfoDto -> {
            ReservationInfo reservationInfo = new ReservationInfo();
            BeanUtils.copyProperties(reservationInfoDto, reservationInfo);
            reservationInfo.setCreateTime(DateUtils.parseTimeToLong(reservationInfoDto.getCreateTime()));
            reservationInfo.setEndTime(DateUtils.parseTimeToLong(reservationInfoDto.getEndTime()));
            reservationInfo.setStartTime(DateUtils.parseTimeToLong(reservationInfoDto.getStartTime()));
            reservationInfo.setUpdateTime(DateUtils.parseTimeToLong(reservationInfoDto.getUpdateTime()));
            reservationInfoList.add(reservationInfo);
        });

        return new ListReservationInfoRep().setResult(CommonResult.SUCCESS.toResult())
                .setTotal(reservations.getTotal())
                .setReservationInfoList(reservationInfoList);
    }
}
