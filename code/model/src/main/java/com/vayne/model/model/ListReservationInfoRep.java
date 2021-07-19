package com.vayne.model.model;

import com.vayne.model.common.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author : Yang Jian
 * @date : 2021/7/6 0006 21:46
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ListReservationInfoRep {
    private Result result;

    private Long total;

    private List<ReservationInfo> reservationInfoList;
}
