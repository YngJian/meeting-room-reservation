package com.vayne.model.model;

import com.vayne.model.common.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author : Yang Jian
 * @date : 2021/7/4 0004 16:56
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UnlockRep {
    private Result result;
}
