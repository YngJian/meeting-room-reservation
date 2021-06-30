package com.vanyne.reservation.application.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vanyne.reservation.infrastruction.repository.db.entity.UserInfo;
import com.vayne.model.model.RegisterRep;
import com.vayne.model.model.RegisterReq;

public interface UserInfoService extends IService<UserInfo> {
    RegisterRep register(RegisterReq registerReq);
}
