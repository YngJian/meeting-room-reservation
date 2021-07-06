package com.vanyne.reservation.infrastruction.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vanyne.reservation.infrastruction.repository.db.entity.MeetRoomInfoEntity;

/**
 * @author : Yang Jian
 * @date : 2021/6/28 0028 22:51
 */
public interface MeetRoomInfoRepository {
    /**
     * 查询列表
     *
     * @param pageNum      页码
     * @param pageSize     条数
     * @param queryWrapper queryWrapper
     * @return int
     */
    IPage<MeetRoomInfoEntity> selectList(Integer pageNum, Integer pageSize, QueryWrapper<MeetRoomInfoEntity> queryWrapper);
}
