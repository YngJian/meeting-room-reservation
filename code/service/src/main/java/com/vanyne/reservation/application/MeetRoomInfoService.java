package com.vanyne.reservation.application;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vanyne.reservation.infrastruction.repository.db.entity.MeetRoomInfoEntity;
import com.vayne.model.model.CreateMeetRoomRep;
import com.vayne.model.model.CreateMeetRoomReq;
import com.vayne.model.model.ListMeetRoomRep;
import com.vayne.model.model.MeetRoomRep;

public interface MeetRoomInfoService extends IService<MeetRoomInfoEntity> {

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
    ListMeetRoomRep listMeetRoom(String roomName, Integer minCapacity, Integer maxCapacity, Integer pageNum, Integer pageSize);

    /**
     * 新增会议室
     *
     * @param createMeetRoomReq createMeetRoomReq
     * @param token             token
     * @return createMeetRoomRep
     */
    CreateMeetRoomRep createMeetRoom(CreateMeetRoomReq createMeetRoomReq, String token);

    /**
     * 禁用会议室信息
     *
     * @param id    id
     * @param token token
     * @return disableMeetRoomRep
     */
    MeetRoomRep disableMeetRoom(Integer id, String token);

    /**
     * 启用会议室信息
     *
     * @param id    id
     * @param token token
     * @return disableMeetRoomRep
     */
    MeetRoomRep enableMeetRoom(Integer id, String token);
}
