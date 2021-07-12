package com.vayne.model.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 会议室预约信息表
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReservationInfo {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 房间id
     */
    private String roomId;

    /**
     * 房间id
     */
    private String roomName;

    /**
     * 用户name
     */
    private String userName;

    /**
     * 是否是当前用户
     */
    private boolean currentUser;

    /**
     * 预约开始时间
     */
    private Long startTime;

    /**
     * 预约结束时间
     */
    private Long endTime;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;
}