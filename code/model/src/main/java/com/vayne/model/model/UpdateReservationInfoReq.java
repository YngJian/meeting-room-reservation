package com.vayne.model.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author : Yang Jian
 * @date : 2021/7/26 0026 22:13
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReservationInfoReq {
    /**
     * 房间id
     */
    @NotNull
    @Size(min = 1, max = 32)
    private String roomId;
    /**
     * 预约开始时间
     */
    @NotNull
    @NotEmpty
    private String startTime;

    /**
     * 预约结束时间
     */
    @NotNull
    @NotEmpty
    private String endTime;
}
