package com.vayne.model.model;

import com.vayne.model.common.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author : Yang Jian
 * @date : 2021/7/12 0006 22:07
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReservationInfoRep {
    private Result result;
}
