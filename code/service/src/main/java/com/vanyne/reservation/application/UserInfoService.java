package com.vanyne.reservation.application;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vanyne.reservation.infrastruction.repository.db.entity.UserInfoEntity;
import com.vayne.model.model.*;

import javax.validation.Valid;

public interface UserInfoService extends IService<UserInfoEntity> {
    /**
     * 注册
     *
     * @param registerReq r
     * @return r
     */
    RegisterRep register(RegisterReq registerReq);

    /**
     * 用户名是否唯一
     *
     * @param userName userName
     * @return r
     */
    RepeatRep userNameRepeated(String userName);

    /**
     * 手机号是否唯一
     *
     * @param phone phone
     * @return r
     */
    RepeatRep phoneRepeated(String phone);

    /**
     * 邮箱是否唯一
     *
     * @param email email
     * @return r
     */
    RepeatRep emailRepeated(String email);

    /**
     * 登录
     *
     * @param loginReq l
     * @return r
     */
    LoginRep login(LoginReq loginReq);

    /**
     * 修改密码
     *
     * @param modifyPwdReq modifyPwdReq
     * @param token        token
     * @return r
     */
    ModifyPwdRep modifyPwd(ModifyPwdReq modifyPwdReq, String token);

    /**
     * 解锁账户
     *
     * @param unlockReq unlockReq
     * @return r
     */
    UnlockRep unlockUser(@Valid UnlockReq unlockReq);

    /**
     * 获取用户信息
     *
     * @param token token
     * @return r
     */
    LoginRep getLoginInfo(String token);
}
