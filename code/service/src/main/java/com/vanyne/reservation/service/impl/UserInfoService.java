package com.vanyne.reservation.service.impl;

import com.vanyne.reservation.repository.db.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vayne.model.model.RegisterRep;
import com.vayne.model.model.RegisterReq;

public interface UserInfoService extends IService<UserInfo>{
    RegisterRep register(RegisterReq registerReq);
}
