package com.vanyne.reservation.infrastruction.repository.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 会议室预约信息表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_reservation_info")
public class ReservationInfoEntity {
    public static final String COL_ID = "id";

    public static final String COL_ROOM_ID = "room_id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_START_TIME = "start_time";

    public static final String COL_END_TIME = "end_time";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 房间id
     */
    @TableField(value = "room_id")
    private String roomId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 预约开始时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 预约结束时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;
}