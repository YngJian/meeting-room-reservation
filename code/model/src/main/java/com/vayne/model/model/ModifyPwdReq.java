package com.vayne.model.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author : Yang Jian
 * @date : 2021/7/4 0004 15:50
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ModifyPwdReq {
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 256)
    private String newPwd;
}
