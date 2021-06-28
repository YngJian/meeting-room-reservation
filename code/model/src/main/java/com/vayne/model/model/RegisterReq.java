package com.vayne.model.model;

import com.vayne.model.common.Result;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author : Yang Jian
 * @date : 2021/6/28 0028 22:38
 */
@Data
@Builder
public class RegisterReq {
    @NotNull
    private String userName;

    @NotNull
    private String password;

    @NotNull
    @Size(min = 1, max = 13)
    private String phone;

    @Email(message = "邮箱格式错误")
    private String email;
}
