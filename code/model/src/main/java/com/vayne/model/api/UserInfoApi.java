package com.vayne.model.api;

import com.vayne.model.model.RegisterRep;
import com.vayne.model.model.RegisterReq;

import javax.validation.Valid;

public interface UserInfoApi {
    RegisterRep register(@Valid RegisterReq registerReq);
}
