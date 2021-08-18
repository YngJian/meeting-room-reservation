package com.vayne.model.api;

import com.vayne.model.model.*;

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

    /**
     * 登录
     *
     * @param loginReq l
     * @return r
     */
    LoginRep login(@Valid @NotNull LoginReq loginReq);

    /**
     * 获取用户信息
     *
     * @param token token
     * @return r
     */
    LoginRep getLoginInfo(String token);

    /**
     * 修改密码
     *
     * @param modifyPwdReq l
     * @param token        l
     * @return r
     */
    ModifyPwdRep modifyPwd(@Valid ModifyPwdReq modifyPwdReq, String token);

    /**
     * 解锁账户
     *
     * @param unlockReq unlockReq
     * @return r
     */
    UnlockRep unlockUser(@Valid UnlockReq unlockReq);
}
