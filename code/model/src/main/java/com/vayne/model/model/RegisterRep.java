package com.vayne.model.model;

import com.vayne.model.common.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author : Yang Jian
 * @date : 2021/6/28 0028 22:38
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRep {
    private Result result;
}
