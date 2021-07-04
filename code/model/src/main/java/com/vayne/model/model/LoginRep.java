package com.vayne.model.model;

import com.vayne.model.common.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author : Yang Jian
 * @date : 2021/7/2 0002 21:22
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginRep {
    private Result result;

    private int remainTimes;

    private boolean pwdWrong;

    private boolean locked;

    private String token;

    private UserInfo userInfo;
}
