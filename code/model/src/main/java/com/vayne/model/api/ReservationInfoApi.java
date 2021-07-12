package com.vayne.model.api;

import com.vayne.model.model.CreateReservationInfoReq;
import com.vayne.model.model.ListReservationInfoRep;
import com.vayne.model.model.ReservationInfoRep;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author : Yang Jian
 * @date : 2021/7/12 0006 22:29
 */
public interface ReservationInfoApi {
    /**
     * 分页查询会议室预约信息
     *
     * @param roomName  会议室名
     * @param userName  用户名
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pageNum   页码
     * @param pageSize  条数
     * @param token     token
     * @return list
     */
    ListReservationInfoRep listReservationInfo(String roomName, String userName, String startTime, String endTime,
                                               @NotNull Integer pageNum, @NotNull Integer pageSize, String token);

    /**
     * 新增会议室
     *
     * @param createReservationInfoReq createReservationInfoReq
     * @param token                    token
     * @return createReservationInfoRep
     */
    ReservationInfoRep createReservationInfo(@Valid CreateReservationInfoReq createReservationInfoReq, String token);

    /**
     * 删除会议室预约信息信息
     *
     * @param id    id
     * @param token token
     * @return disableReservationInfoRep
     */
    ReservationInfoRep deleteReservationInfo(@NotNull @NotEmpty Integer id, String token);
}
