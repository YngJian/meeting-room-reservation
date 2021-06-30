package com.vayne.model.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author : Yang Jian
 * @date : 2021/6/28 0028 22:38
 */
@Data
public class RegisterReq {
    @NotNull
    private String userName;

    @NotNull
    @Size(min = 1, max = 256)
    private String password;

    @NotNull
    @Pattern(regexp = "1[0-9]{10}", message = "手机号格式不正确！")
    private String phone;

    @NotNull
    @Email(message = "邮箱格式错误")
    private String email;
}
