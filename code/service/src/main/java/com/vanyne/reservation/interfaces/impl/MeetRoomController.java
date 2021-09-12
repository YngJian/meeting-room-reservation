package com.vanyne.reservation.interfaces.impl;

import com.vanyne.reservation.application.MeetRoomInfoService;
import com.vayne.model.api.MeetRoomApi;
import com.vayne.model.model.CreateMeetRoomRep;
import com.vayne.model.model.CreateMeetRoomReq;
import com.vayne.model.model.ListMeetRoomRep;
import com.vayne.model.model.MeetRoomRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author : Yang Jian
 * @date : 2021/7/6 0006 22:09
 */
@RestController
@RequestMapping("/room")
public class MeetRoomController implements MeetRoomApi {
    private final static Integer DEFAULT_PAGE_NUM = 1;

    private final static Integer DEFAULT_PAGE_SIZE = 10;

    @Autowired
    private MeetRoomInfoService meetRoomInfoService;

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
    @GetMapping("/list")
    @Override
    public ListMeetRoomRep listMeetRoom(@RequestParam(value = "roomName", required = false) String roomName,
                                        @RequestParam(value = "minCapacity", required = false) Integer minCapacity,
                                        @RequestParam(value = "maxCapacity", required = false) Integer maxCapacity,
                                        @RequestParam(required = false) Integer pageNum,
                                        @RequestParam(required = false) Integer pageSize) {
        pageNum = pageNum == null ? DEFAULT_PAGE_NUM : pageNum;
        pageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;
        return meetRoomInfoService.listMeetRoom(roomName, minCapacity, maxCapacity, pageNum, pageSize);
    }

    /**
     * 新增会议室
     *
     * @param createMeetRoomReq createMeetRoomReq
     * @return createMeetRoomRep
     */
    @PostMapping("/add")
    @Override
    public CreateMeetRoomRep createMeetRoom(@Valid @RequestBody CreateMeetRoomReq createMeetRoomReq,
                                            @RequestHeader("token") String token) {
        return meetRoomInfoService.createMeetRoom(createMeetRoomReq, token);
    }

    /**
     * 禁用会议室信息
     *
     * @param id    id
     * @param token token
     * @return disableMeetRoomRep
     */
    @PutMapping("/disable/{id}")
    @Override
    public MeetRoomRep disableMeetRoom(@PathVariable @NotNull @NotEmpty Integer id,
                                       @RequestHeader("token") String token) {
        return meetRoomInfoService.disableMeetRoom(id, token);
    }

    /**
     * 启用会议室信息
     *
     * @param id    id
     * @param token token
     * @return disableMeetRoomRep
     */
    @PutMapping("/enable/{id}")
    @Override
    public MeetRoomRep enableMeetRoom(@PathVariable @NotNull @NotEmpty Integer id,
                                      @RequestHeader("token") String token) {
        return meetRoomInfoService.enableMeetRoom(id, token);
    }
}
