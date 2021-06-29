package com.vayne.model.model;

import com.vayne.model.common.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String password;

    @NotNull
    @Size(min = 1, max = 13)
    @Pattern(regexp = "1[0-9]{10}", message = "只能是数字")
    private String phone;

    @Email(message = "邮箱格式错误")
    private String email;
}
