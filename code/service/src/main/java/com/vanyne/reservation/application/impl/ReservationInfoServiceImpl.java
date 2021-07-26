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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

        Optional<Date> startDate = DateUtils.parse(startTime);
        Optional<Date> endDate = DateUtils.parse(endTime);
        if (!startDate.isPresent() || !endDate.isPresent()) {
            log.info("The time format is incorrect.startTime:[{}],endTime:[{}]", startTime, endTime);
            return new ReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "The time format is incorrect.")
                    );
        }

        ReservationInfoRep reservationInfoRep = this.isCorrectTime(startDate.get(), endDate.get(), startTime, endTime);
        if (reservationInfoRep != null) {
            return reservationInfoRep;
        }

        String userId = userInfo.getUserId();
        int countByUserId = userInfoRepository.selectCountByUserId(userId);
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

        // 判断时间是否有被预约
        if (isAppointmentTimePeriod(startTime, endTime, roomId)) {
            return new ReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "This meeting room has been reserved for this time period.")
                    );
        }

        // 没被预约，抢锁
        String value = CommonUtils.getUUID();
        String key = ConstantType.RESERVATION_INFO_CREATE + roomId;
        try {
            boolean lock = redisLock.lock(key, value, ConstantType.RESERVATION_INFO_ROCK,
                    ConstantType.RESERVATION_INFO_CREATE_EXPIRE);
            if (!lock) {
                log.info("Failed to reserve a meeting room, try again later.roomId: [{}],startTime:[{}],endTime:[{}]"
                        , roomId, startTime, endTime);
                return new ReservationInfoRep()
                        .setResult(
                                new Result(CommonResult.INVALID_PARAM.getCode(),
                                        "Failed to reserve a meeting room, try again later.")
                        );
            }
            // 判断时间是否有被预约
            if (isAppointmentTimePeriod(startTime, endTime, roomId)) {
                return new ReservationInfoRep()
                        .setResult(
                                new Result(CommonResult.INVALID_PARAM.getCode(),
                                        "This meeting room has been reserved for this time period.")
                        );
            }
            this.insertByEntity(roomId, startDate.get(), endDate.get(), userId);
        } finally {
            redisLock.unlock(key, value);
        }
        return new ReservationInfoRep()
                .setResult(CommonResult.SUCCESS.toResult());
    }

    private ReservationInfoRep isCorrectTime(Date startDate, Date endDate, String startTime, String endTime) {
        LocalDateTime localDateStart = DateUtils.dateToLocalDateTime(startDate);
        LocalDateTime localDateEnd = DateUtils.dateToLocalDateTime(endDate);
        if (localDateStart.isAfter(localDateEnd) || localDateStart.equals(localDateEnd)) {
            log.info("Start time should be less than end time.startTime:[{}],endTime:[{}]", startTime, endTime);
            return new ReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "Start time should be less than end time.")
                    );
        }
        LocalDate localDate = LocalDate.now();
        LocalDateTime zeroPoint = LocalDateTime.of(localDate, LocalTime.MIN);
        if (zeroPoint.isAfter(localDateStart)) {
            log.info("Can only reserve the meeting room on the day.startTime:[{}],endTime:[{}]", startTime, endTime);
            return new ReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "Can only reserve the meeting room on the day.")
                    );
        }

        LocalDateTime TwentyFourOClock = LocalDateTime.of(localDate, LocalTime.MAX);
        if (localDateEnd.isAfter(TwentyFourOClock)) {
            log.info("The appointment time can only be today.startTime:[{}],endTime:[{}]", startTime, endTime);
            return new ReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "The appointment time can only be today.")
                    );
        }
        return null;
    }

    private void insertByEntity(String roomId, Date startDate, Date endDate, String userId) {
        Date time = new Date();
        ReservationInfoEntity reservationInfoEntity = ReservationInfoEntity.builder()
                .roomId(roomId)
                .startTime(startDate)
                .endTime(endDate)
                .roomId(roomId)
                .userId(userId)
                .createTime(time)
                .updateTime(time)
                .build();
        this.save(reservationInfoEntity);
    }

    /**
     * 删除会议室预约信息信息
     *
     * @param id    id
     * @param token token
     * @return disableReservationInfoRep
     */
    @Override
    public ReservationInfoRep deleteReservationInfo(Integer id, String token) {
        ReservationInfoEntity reservationInfoEntity = this.getById(id);
        if (reservationInfoEntity == null) {
            log.info("id does not exist.id:[{}]", id);
            return new ReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "id does not exist.!")
                    );
        }

        ReservationInfoRep validParam = isValidParam(id, token, reservationInfoEntity.getUserId());
        if (validParam != null) {
            return validParam;
        }

        this.removeById(id);
        return new ReservationInfoRep().setResult(CommonResult.SUCCESS.toResult());
    }

    /**
     * 修改会议室预约信息信息
     *
     * @param id                       id
     * @param token                    token
     * @param updateReservationInfoReq u
     * @return disableReservationInfoRep
     */
    @Override
    public ReservationInfoRep updateReservationInfo(Integer id, String token, UpdateReservationInfoReq updateReservationInfoReq) {
        ReservationInfoEntity reservationInfoEntity = this.getById(id);
        if (reservationInfoEntity == null) {
            log.info("id does not exist.id:[{}]", id);
            return new ReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "id does not exist.!")
                    );
        }

        ReservationInfoRep validParam = this.isValidParam(id, token, reservationInfoEntity.getUserId());
        if (validParam != null) {
            return validParam;
        }
        String startTime = updateReservationInfoReq.getStartTime();
        String endTime = updateReservationInfoReq.getEndTime();
        String roomId = updateReservationInfoReq.getRoomId();

        Optional<Date> startDate = DateUtils.parse(startTime);
        Optional<Date> endDate = DateUtils.parse(endTime);
        if (!startDate.isPresent() || !endDate.isPresent()) {
            log.info("The time format is incorrect.startTime:[{}],endTime:[{}]", startTime, endTime);
            return new ReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "The time format is incorrect.")
                    );
        }

        ReservationInfoRep reservationInfoRep = this.isCorrectTime(startDate.get(), endDate.get(), startTime, endTime);
        if (reservationInfoRep != null) {
            return reservationInfoRep;
        }

        if (isModifyTheDay(startTime, endTime, reservationInfoEntity.getStartTime())) {
            return new ReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "Can only modify the appointment for the day.")
                    );
        }

        ReservationInfoRep idMatchRoomId = this.idMatchRoomId(reservationInfoEntity.getRoomId(), id, roomId);
        if (idMatchRoomId != null) {
            return idMatchRoomId;
        }

        // 判断时间是否有被预约
        if (isAppointmentTimePeriod(startTime, endTime, roomId)) {
            return new ReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "This meeting room has been reserved for this time period.")
                    );
        }

        // 没被预约，抢锁
        String value = CommonUtils.getUUID();
        String key = ConstantType.RESERVATION_INFO_CREATE + roomId;
        try {
            boolean lock = redisLock.lock(key, value, ConstantType.RESERVATION_INFO_ROCK,
                    ConstantType.RESERVATION_INFO_CREATE_EXPIRE);
            if (!lock) {
                log.info("Failed to reserve a meeting room, try again later.roomId: [{}],startTime:[{}],endTime:[{}]"
                        , roomId, startTime, endTime);
                return new ReservationInfoRep()
                        .setResult(
                                new Result(CommonResult.INVALID_PARAM.getCode(),
                                        "Failed to reserve a meeting room, try again later.")
                        );
            }
            // 判断时间是否有被预约
            if (isAppointmentTimePeriod(startTime, endTime, roomId)) {
                return new ReservationInfoRep()
                        .setResult(
                                new Result(CommonResult.INVALID_PARAM.getCode(),
                                        "This meeting room has been reserved for this time period.")
                        );
            }
            this.updateEntityById(id, startDate.get(), endDate.get());
        } finally {
            redisLock.unlock(key, value);
        }
        return new ReservationInfoRep()
                .setResult(CommonResult.SUCCESS.toResult());
    }

    private boolean isModifyTheDay(String startTime, String endTime, Date infoStartTime) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime zeroPoint = LocalDateTime.of(localDate, LocalTime.MIN);
        if (DateUtils.dateToLocalDateTime(infoStartTime).isBefore(zeroPoint)) {
            log.info("Can only modify the appointment for the day.startTime:[{}],endTime:[{}]", startTime, endTime);
            return true;
        }
        return false;
    }

    private void updateEntityById(Integer id, Date startDate, Date endDate) {
        ReservationInfoEntity reservationInfoEntity = ReservationInfoEntity.builder()
                .id(id)
                .startTime(startDate)
                .endTime(endDate)
                .updateTime(new Date())
                .build();
        this.updateById(reservationInfoEntity);
    }

    private boolean isAppointmentTimePeriod(String startTime, String endTime, String roomId) {
        int count = reservationInfoRepository.IsAppointment(startTime, endTime, roomId);
        if (count > 0) {
            log.info("This meeting room has been reserved for this time period.roomId: [{}]", roomId);
            return true;
        }
        return false;
    }

    private ReservationInfoRep isValidParam(Integer id, String token, String userId) {
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
        String userInfoUserId = userInfo.getUserId();

        if (!userInfoUserId.equals(userId)) {
            log.info("Only allow to delete own appointments.token:[{}]", token);
            return new ReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "Only allow to delete own appointments!")
                    );
        }
        return null;
    }

    private ReservationInfoRep idMatchRoomId(String infoRoomId, Integer id, String roomId) {
        if (!roomId.equals(infoRoomId)) {
            log.info("Id and room id do not match.id:[{}], roomId: [{}]", id, roomId);
            return new ReservationInfoRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "Id and room id do not match.")
                    );
        }
        return null;
    }
}
