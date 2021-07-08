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
    @Size(min = 1, max = 64)
    private String userName;

    @NotNull
    @Size(min = 1, max = 256)
    private String password;

    @NotNull
    @Pattern(regexp = "1[0-9]{10}", message = "Phone number format is incorrect!")
    private String phone;

    @NotNull
    @NotEmpty
    @Email(message = "Email format error!")
    private String email;
}
