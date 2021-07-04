package com.vayne.model.model;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * @author : Yang Jian
 * @date : 2021/6/28 0028 22:38
 */
@Data
public class RegisterReq {
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 64)
    private String userName;

    @NotNull
    @Size(min = 1, max = 256)
    private String password;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "1[0-9]{10}", message = "手机号格式不正确！")
    private String phone;

    @NotNull
    @NotEmpty
    @Email(message = "邮箱格式错误")
    private String email;
}
