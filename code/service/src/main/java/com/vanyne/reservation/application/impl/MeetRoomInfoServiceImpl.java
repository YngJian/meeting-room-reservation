package com.vanyne.reservation.application.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanyne.reservation.application.MeetRoomInfoService;
import com.vanyne.reservation.domain.enums.CommonResult;
import com.vanyne.reservation.infrastruction.common.ConstantType;
import com.vanyne.reservation.infrastruction.common.StatusType;
import com.vanyne.reservation.infrastruction.repository.MeetRoomInfoRepository;
import com.vanyne.reservation.infrastruction.repository.db.entity.MeetRoomInfoEntity;
import com.vanyne.reservation.infrastruction.repository.db.mapper.MeetRoomInfoMapper;
import com.vanyne.reservation.infrastruction.util.CommonUtils;
import com.vayne.model.common.Result;
import com.vayne.model.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MeetRoomInfoServiceImpl extends ServiceImpl<MeetRoomInfoMapper, MeetRoomInfoEntity> implements MeetRoomInfoService {
    @Autowired
    private MeetRoomInfoRepository meetRoomInfoRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分页查询会议室信息
     *
     * @param roomName    会议室名
     * @param minCapacity 最小容量
     * @param maxCapacity 最大容量
     * @param pageNum     页码
     * @param pageSize    条数
     * @return list
     */
    @Override
    public ListMeetRoomRep listMeetRoom(String roomName, Integer minCapacity, Integer maxCapacity, Integer pageNum, Integer pageSize) {
        QueryWrapper<MeetRoomInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight(!StringUtils.isEmpty(roomName), MeetRoomInfoEntity.COL_ROOM_NAME, roomName)
                .le(maxCapacity != null, MeetRoomInfoEntity.COL_CAPACITY, maxCapacity)
                .ge(minCapacity != null, MeetRoomInfoEntity.COL_CAPACITY, minCapacity);
        IPage<MeetRoomInfoEntity> meetRoomInfoEntityIPage = meetRoomInfoRepository.selectList(pageNum, pageSize, queryWrapper);

        List<MeetRoomInfoEntity> records = meetRoomInfoEntityIPage.getRecords();
        List<MeetRoomInfo> meetRoomInfos = new ArrayList<>(records.size());

        records.forEach(meetRoomInfoEntity -> {
            MeetRoomInfo meetRoomInfo = new MeetRoomInfo();
            BeanUtils.copyProperties(meetRoomInfoEntity, meetRoomInfo);
            meetRoomInfo.setCreateTime(parseLongTime(meetRoomInfoEntity.getCreateTime()));
            meetRoomInfo.setUpdateTime(parseLongTime(meetRoomInfoEntity.getUpdateTime()));
            meetRoomInfos.add(meetRoomInfo);
        });

        return new ListMeetRoomRep()
                .setResult(CommonResult.SUCCESS.toResult())
                .setTotal((int) meetRoomInfoEntityIPage.getTotal())
                .setMeetRoomInfo(meetRoomInfos);
    }

    private Long parseLongTime(Date date) {
        return date == null ? null : date.getTime();
    }

    /**
     * 新增会议室
     *
     * @param createMeetRoomReq createMeetRoomReq
     * @param token             token
     * @return createMeetRoomRep
     */
    @Override
    public CreateMeetRoomRep createMeetRoom(CreateMeetRoomReq createMeetRoomReq, String token) {
        String user = stringRedisTemplate.opsForValue().get(ConstantType.TOKEN_KEY + token);
        if (StringUtils.isEmpty(user)) {
            log.info("The token [{}] has expired, please log in again!", token);
            return new CreateMeetRoomRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "The token has expired, please log in again!")
                    );
        }

        UserInfo userInfo = JSONUtil.toBean(user, UserInfo.class);
        String roomName = createMeetRoomReq.getRoomName();
        Integer capacity = createMeetRoomReq.getCapacity();
        Date date = new Date();
        MeetRoomInfoEntity roomInfoEntity = MeetRoomInfoEntity.builder()
                .roomId(CommonUtils.getUUID())
                .capacity(capacity)
                .roomName(roomName)
                .status(StatusType.NORMAL)
                .updateUser(userInfo.getUserId())
                .createTime(date)
                .updateTime(date)
                .build();

        boolean save = this.save(roomInfoEntity);
        if (!save) {
            log.info("Failed to save database!");
            return new CreateMeetRoomRep()
                    .setResult(
                            new Result(CommonResult.FAILED.getCode(),
                                    "Failed to save database!")
                    );
        }

        return new CreateMeetRoomRep()
                .setResult(CommonResult.SUCCESS.toResult());
    }

    /**
     * 禁用会议室信息
     *
     * @param id    id
     * @param token token
     * @return disableMeetRoomRep
     */
    @Override
    public MeetRoomRep disableMeetRoom(Integer id, String token) {
        return getMeetRoomRep(id, token, StatusType.DISABLE);
    }

    /**
     * 启用会议室信息
     *
     * @param id    id
     * @param token token
     * @return disableMeetRoomRep
     */
    @Override
    public MeetRoomRep enableMeetRoom(Integer id, String token) {
        return getMeetRoomRep(id, token, StatusType.NORMAL);
    }

    private MeetRoomRep getMeetRoomRep(Integer id, String token, Integer status) {
        String user = stringRedisTemplate.opsForValue().get(ConstantType.TOKEN_KEY + token);
        if (StringUtils.isEmpty(user)) {
            log.info("The token [{}] has expired, please log in again!", token);
            return new MeetRoomRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "The token has expired, please log in again!")
                    );
        }

        MeetRoomInfoEntity roomInfoEntity = this.getById(id);
        if (roomInfoEntity == null) {
            log.info("Id [{}] does not exist!", id);
            return new MeetRoomRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "Id does not exist!")
                    );
        }

        UserInfo userInfo = JSONUtil.toBean(user, UserInfo.class);
        Date date = new Date();
        MeetRoomInfoEntity meetRoomInfoEntity = MeetRoomInfoEntity.builder()
                .id(id)
                .status(status)
                .updateUser(userInfo.getUserId())
                .updateTime(date)
                .build();

        boolean update = this.updateById(meetRoomInfoEntity);
        if (!update) {
            log.info("Failed to update database!");
            return new MeetRoomRep()
                    .setResult(
                            new Result(CommonResult.FAILED.getCode(),
                                    "Failed to update database!")
                    );
        }

        return new MeetRoomRep()
                .setResult(CommonResult.SUCCESS.toResult());
    }
}
