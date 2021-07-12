package com.vanyne.reservation.interfaces.impl;

import com.vanyne.reservation.application.ReservationInfoService;
import com.vanyne.reservation.domain.enums.CommonResult;
import com.vanyne.reservation.infrastruction.repository.db.ReservationInfoDto;
import com.vanyne.reservation.infrastruction.util.ReservationUtils;
import com.vayne.model.api.ReservationInfoApi;
import com.vayne.model.model.CreateReservationInfoReq;
import com.vayne.model.model.ListReservationInfoRep;
import com.vayne.model.model.ReservationInfoRep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author : Yang Jian
 * @date : 2021/7/12 0012 22:18
 */
@RestController
@RequestMapping("/reservation")
@Slf4j
public class ReservationInfoController implements ReservationInfoApi {
    @Autowired
    private ReservationInfoService reservationInfoService;

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
    @GetMapping("/list")
    @Override
    public ListReservationInfoRep listReservationInfo(@RequestParam(required = false) String roomName,
                                                      @RequestParam(required = false) String userName,
                                                      @RequestParam(required = false) String startTime,
                                                      @RequestParam(required = false) String endTime,
                                                      @NotNull Integer pageNum, @NotNull Integer pageSize,
                                                      @RequestHeader("token") String token) {
        ReservationInfoDto reservationInfoDto = new ReservationInfoDto();
        if (ReservationUtils.isFormat(startTime, reservationInfoDto, "The start time format is incorrect.")) {
            return new ListReservationInfoRep().setResult(CommonResult.INVALID_PARAM.toResult());
        }
        if (ReservationUtils.isFormat(endTime, reservationInfoDto, "The endTime time format is incorrect.")) {
            return new ListReservationInfoRep().setResult(CommonResult.INVALID_PARAM.toResult());
        }
        reservationInfoDto.setUserName(userName)
                .setRoomName(roomName);
        return reservationInfoService.listReservationInfo(token, reservationInfoDto, pageNum, pageSize);
    }

    /**
     * 新增会议室
     *
     * @param createReservationInfoReq createReservationInfoReq
     * @param token                    token
     * @return createReservationInfoRep
     */
    @Override
    public ReservationInfoRep createReservationInfo(@RequestBody @Valid CreateReservationInfoReq createReservationInfoReq,
                                                    @RequestHeader("token") String token) {
        return null;
    }

    /**
     * 删除会议室预约信息信息
     *
     * @param id    id
     * @param token token
     * @return disableReservationInfoRep
     */
    @Override
    public ReservationInfoRep deleteReservationInfo(@NotNull @NotEmpty Integer id,
                                                    @RequestHeader("token") String token) {
        return null;
    }
}
