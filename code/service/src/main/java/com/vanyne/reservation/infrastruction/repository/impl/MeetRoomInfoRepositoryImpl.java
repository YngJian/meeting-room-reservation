package com.vanyne.reservation.infrastruction.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vanyne.reservation.infrastruction.repository.MeetRoomInfoRepository;
import com.vanyne.reservation.infrastruction.repository.db.entity.MeetRoomInfoEntity;
import com.vanyne.reservation.infrastruction.repository.db.mapper.MeetRoomInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @author : Yang Jian
 * @date : 2021/7/6 0006 22:29
 */
@Repository
public class MeetRoomInfoRepositoryImpl implements MeetRoomInfoRepository {
    @Autowired
    private MeetRoomInfoMapper meetRoomInfoMapper;

    /**
     * 查询列表
     *
     * @param roomName    会议室名
     * @param minCapacity 最小容量
     * @param maxCapacity 最大容量
     * @param pageNum     页码
     * @param pageSize    条数
     * @return int
     */
    @Override
    public IPage<MeetRoomInfoEntity> selectList(String roomName, Integer minCapacity, Integer maxCapacity,
                                                Integer pageNum, Integer pageSize) {
        Page<MeetRoomInfoEntity> page = new Page<>(pageNum, pageSize, true);

        QueryWrapper<MeetRoomInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight(!StringUtils.isEmpty(roomName), MeetRoomInfoEntity.COL_ROOM_NAME, roomName)
                .le(maxCapacity != null, MeetRoomInfoEntity.COL_CAPACITY, maxCapacity)
                .ge(minCapacity != null, MeetRoomInfoEntity.COL_CAPACITY, minCapacity)
                .orderByDesc(MeetRoomInfoEntity.COL_UPDATE_TIME)
                .orderByAsc(MeetRoomInfoEntity.COL_ROOM_NAME);

        return meetRoomInfoMapper.selectPage(page, queryWrapper);
    }

    /**
     * 根据roomId查询
     *
     * @param roomId roomId
     * @return int
     */
    @Override
    public int selectCountByRoomId(String roomId) {
        QueryWrapper<MeetRoomInfoEntity> userInfoWrapper = new QueryWrapper<>();
        userInfoWrapper.eq(MeetRoomInfoEntity.COL_ROOM_ID, roomId);
        return meetRoomInfoMapper.selectCount(userInfoWrapper);
    }

    /**
     * 根据roomName查询
     *
     * @param roomName roomName
     * @return int
     */
    @Override
    public int selectCountByRoomName(String roomName) {
        QueryWrapper<MeetRoomInfoEntity> userInfoWrapper = new QueryWrapper<>();
        userInfoWrapper.eq(MeetRoomInfoEntity.COL_ROOM_NAME, roomName);
        return meetRoomInfoMapper.selectCount(userInfoWrapper);
    }
}
