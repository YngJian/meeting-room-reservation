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
 * 会议室信息表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_meet_room_info")
public class MeetRoomInfoEntity {
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
     * 房间名
     */
    @TableField(value = "room_name")
    private String roomName;

    /**
     * 容量
     */
    @TableField(value = "capacity")
    private Byte capacity;

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

    public static final String COL_ID = "id";

    public static final String COL_ROOM_ID = "room_id";

    public static final String COL_ROOM_NAME = "room_name";

    public static final String COL_CAPACITY = "capacity";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}