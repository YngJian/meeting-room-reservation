package com.vayne.model.api;

import com.vayne.model.model.CreateMeetRoomRep;
import com.vayne.model.model.CreateMeetRoomReq;
import com.vayne.model.model.ListMeetRoomRep;
import com.vayne.model.model.MeetRoomRep;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author : Yang Jian
 * @date : 2021/7/6 0006 21:43
 */
public interface MeetRoomApi {
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
    ListMeetRoomRep listMeetRoom(String roomName, Integer minCapacity, Integer maxCapacity, @NotNull Integer pageNum,
                                 @NotNull Integer pageSize);

    /**
     * 新增会议室
     *
     * @param createMeetRoomReq createMeetRoomReq
     * @param token             token
     * @return createMeetRoomRep
     */
    CreateMeetRoomRep createMeetRoom(@Valid CreateMeetRoomReq createMeetRoomReq, String token);

    /**
     * 禁用会议室信息
     *
     * @param id    id
     * @param token token
     * @return disableMeetRoomRep
     */
    MeetRoomRep disableMeetRoom(@NotNull @NotEmpty Integer id, String token);

    /**
     * 启用会议室信息
     *
     * @param id    id
     * @param token token
     * @return disableMeetRoomRep
     */
    MeetRoomRep enableMeetRoom(@NotNull @NotEmpty Integer id, String token);
}
