package com.vayne.model.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author : Yang Jian
 * @date : 2021/7/2 0002 21:18
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginReq {
    @NotNull
    private String userName;

    @NotNull
    @Size(min = 1, max = 256)
    private String password;
}
