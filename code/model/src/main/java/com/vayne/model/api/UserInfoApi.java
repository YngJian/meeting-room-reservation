package com.vayne.model.api;

import com.vayne.model.model.RegisterRep;
import com.vayne.model.model.RegisterReq;
import com.vayne.model.model.RepeatRep;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface UserInfoApi {
    /**
     * 注册
     *
     * @param registerReq r
     * @return r
     */
    RegisterRep register(@Valid RegisterReq registerReq);

    /**
     * 用户名是否唯一
     *
     * @param userName userName
     * @return r
     */
    RepeatRep userNameRepeated(@NotNull String userName);

    /**
     * 手机号是否唯一
     *
     * @param phone phone
     * @return r
     */
    RepeatRep phoneRepeated(@NotNull String phone);

    /**
     * 邮箱是否唯一
     *
     * @param email email
     * @return r
     */
    RepeatRep emailRepeated(@NotNull String email);
}
