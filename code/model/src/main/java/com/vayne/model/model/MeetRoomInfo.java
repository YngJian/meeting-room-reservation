package com.vayne.model.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author : Yang Jian
 * @date : 2021/7/6 0006 21:47
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MeetRoomInfo {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 房间名
     */
    private String roomName;

    /**
     * 容量
     */
    private Integer capacity;

    /**
     * 会议室状态：1正常，0禁用
     */
    private Integer status;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;
}
