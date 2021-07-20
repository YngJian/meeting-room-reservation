package com.vanyne.reservation.application.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanyne.reservation.application.ReservationInfoService;
import com.vanyne.reservation.domain.enums.CommonResult;
import com.vanyne.reservation.infrastruction.common.ConstantType;
import com.vanyne.reservation.infrastruction.repository.MeetRoomInfoRepository;
import com.vanyne.reservation.infrastruction.repository.ReservationInfoRepository;
import com.vanyne.reservation.infrastruction.repository.UserInfoRepository;
import com.vanyne.reservation.infrastruction.repository.db.dto.ReservationInfoDto;
import com.vanyne.reservation.infrastruction.repository.db.dto.ReservationInfoQo;
import com.vanyne.reservation.infrastruction.repository.db.entity.ReservationInfoEntity;
import com.vanyne.reservation.infrastruction.repository.db.mapper.ReservationInfoMapper;
import com.vanyne.reservation.infrastruction.util.CommonUtils;
import com.vanyne.reservation.infrastruction.util.DateUtils;
import com.vanyne.reservation.infrastruction.util.RedisLock;
import com.vayne.model.common.Result;
import com.vayne.model.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReservationInfoServiceImpl extends ServiceImpl<ReservationInfoMapper, ReservationInfoEntity> implements ReservationInfoService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ReservationInfoRepository reservationInfoRepository;

    @Autowired
    private MeetRoomInfoRepository meetRoomInfoRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private RedisLock redisLock;

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

    /**
     * 创建会议室预约信息
     *
     * @param createReservationInfoReq createReservationInfoReq
     * @param token                    token
     * @return ReservationInfoRep
     */
    @Override
    public ReservationInfoRep createReservationInfo(CreateReservationInfoReq createReservationInfoReq, String token) {
        String user = stringRedisTemplate.opsForValue().get(ConstantType.TOKEN_KEY + token);
        if (StringUtils.isEmpty(user)) {
            log.info("The token [{}] has expired, please log in again!", token);
            return new ReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "The token has expired, please log in again!")
                    );
        }
        UserInfo userInfo = JSONUtil.toBean(user, UserInfo.class);
        String roomId = createReservationInfoReq.getRoomId();
        String startTime = createReservationInfoReq.getStartTime();
        String endTime = createReservationInfoReq.getEndTime();

        if (!DateUtils.isCorrectFormat(startTime) || !DateUtils.isCorrectFormat(endTime)) {
            log.info("The time format is incorrect.startTime:[{}],endTime:[{}]", startTime, endTime);
            return new ReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "The time format is incorrect.")
                    );
        }

        int countByUserId = userInfoRepository.selectCountByUserId(userInfo.getUserId());
        if (countByUserId == 0) {
            log.info("The user id does not exist.token: [{}]", token);
            return new ReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "The user id does not exist.")
                    );
        }

        int countByRoomId = meetRoomInfoRepository.selectCountByRoomId(roomId);
        if (countByRoomId == 0) {
            log.info("The room id does not exist.roomId: [{}]", roomId);
            return new ReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "The room id does not exist.")
                    );
        }

        String value = CommonUtils.getUUID();
        boolean lock = redisLock.lock(ConstantType.RESERVATION_INFO_CREATE, value, 1, 60);
        if (lock) {
            // 判断时间是否有被预约
        }
        return new ReservationInfoRep().setResult(CommonResult.SUCCESS.toResult());
    }
}
