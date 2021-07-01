package com.vanyne.reservation.application.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vanyne.reservation.infrastruction.repository.db.entity.UserInfo;
import com.vayne.model.model.RegisterRep;
import com.vayne.model.model.RegisterReq;
import com.vayne.model.model.RepeatRep;

public interface UserInfoService extends IService<UserInfo> {
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
}
