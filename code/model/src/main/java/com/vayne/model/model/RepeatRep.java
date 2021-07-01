package com.vayne.model.model;

import com.vayne.model.common.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author : Yang Jian
 * @date : 2021/7/1 0001 21:33
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepeatRep {
    private Result result;
    private boolean repeated;
}
