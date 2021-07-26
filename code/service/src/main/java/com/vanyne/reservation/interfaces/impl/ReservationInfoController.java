package com.vanyne.reservation.interfaces.impl;

import com.vanyne.reservation.application.ReservationInfoService;
import com.vanyne.reservation.domain.enums.CommonResult;
import com.vanyne.reservation.infrastruction.repository.db.dto.ReservationInfoQo;
import com.vanyne.reservation.infrastruction.util.DateUtils;
import com.vanyne.reservation.infrastruction.util.ReservationUtils;
import com.vayne.model.api.ReservationInfoApi;
import com.vayne.model.common.Result;
import com.vayne.model.model.CreateReservationInfoReq;
import com.vayne.model.model.ListReservationInfoRep;
import com.vayne.model.model.ReservationInfoRep;
import com.vayne.model.model.UpdateReservationInfoReq;
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
     * @return list
     */
    @GetMapping("/list")
    @Override
    public ListReservationInfoRep listReservationInfo(@RequestParam(required = false) String roomName,
                                                      @RequestParam(required = false) String userName,
                                                      @RequestParam(required = false) String startTime,
                                                      @RequestParam(required = false) String endTime,
                                                      @NotNull @RequestParam Integer pageNum,
                                                      @NotNull @RequestParam Integer pageSize) {
        ReservationInfoQo reservationInfoQo = new ReservationInfoQo();

        String msg = "The start time format is incorrect.";
        if (ReservationUtils.isFormat(startTime, reservationInfoQo, msg)) {
            return new ListReservationInfoRep().setResult(new Result(CommonResult.INVALID_PARAM.getCode(), msg));
        }

        String endMsg = "The endTime time format is incorrect.";
        if (ReservationUtils.isFormat(endTime, reservationInfoQo, endMsg)) {
            return new ListReservationInfoRep().setResult(new Result(CommonResult.INVALID_PARAM.getCode(), endMsg));
        }

        String lessMsg = "The start time should be less than the end time.";
        if (DateUtils.compareTime(reservationInfoQo.getStartTime(), reservationInfoQo.getEndTime())) {
            return new ListReservationInfoRep().setResult(new Result(CommonResult.INVALID_PARAM.getCode(), lessMsg));
        }

        reservationInfoQo.setUserName(userName)
                .setRoomName(roomName);
        return reservationInfoService.listReservationInfo(reservationInfoQo, pageNum, pageSize);
    }

    /**
     * 新增会议室
     *
     * @param createReservationInfoReq createReservationInfoReq
     * @param token                    token
     * @return createReservationInfoRep
     */
    @PostMapping("/add")
    @Override
    public ReservationInfoRep createReservationInfo(@RequestBody @Valid CreateReservationInfoReq createReservationInfoReq,
                                                    @RequestHeader("token") String token) {
        return reservationInfoService.createReservationInfo(createReservationInfoReq, token);
    }

    /**
     * 删除会议室预约信息信息
     *
     * @param id    id
     * @param token token
     * @return disableReservationInfoRep
     */
    @DeleteMapping("/delete/{id}")
    @Override
    public ReservationInfoRep deleteReservationInfo(@NotNull @NotEmpty @PathVariable Integer id,
                                                    @RequestHeader("token") String token) {
        return reservationInfoService.deleteReservationInfo(id, token);
    }

    /**
     * 修改会议室预约信息信息
     *
     * @param id    id
     * @param token token
     * @return disableReservationInfoRep
     */
    @PutMapping("/update/{id}")
    @Override
    public ReservationInfoRep updateReservationInfo(@NotNull @NotEmpty @PathVariable Integer id,
                                                    @RequestHeader("token") String token,
                                                    @RequestBody @Valid UpdateReservationInfoReq updateReservationInfoReq) {
        return reservationInfoService.updateReservationInfo(id, token, updateReservationInfoReq);
    }
}
