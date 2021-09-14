package com.vanyne.reservation.infrastruction.repository;

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
     * @param roomName    会议室名
     * @param minCapacity 最小容量
     * @param maxCapacity 最大容量
     * @param pageNum     页码
     * @param pageSize    条数
     * @return int
     */
    IPage<MeetRoomInfoEntity> selectList(String roomName, Integer minCapacity, Integer maxCapacity,
                                         Integer pageNum, Integer pageSize);

    /**
     * 根据roomId查询
     *
     * @param roomId userId
     * @return int
     */
    int selectCountByRoomId(String roomId);

    /**
     * 根据roomName查询
     *
     * @param roomName roomName
     * @return int
     */
    int selectCountByRoomName(String roomName);
}
