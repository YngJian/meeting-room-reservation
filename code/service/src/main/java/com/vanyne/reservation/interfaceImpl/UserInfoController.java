package com.vanyne.reservation.interfaceImpl;

import com.vanyne.reservation.service.impl.UserInfoService;
import com.vayne.model.api.UserInfoApi;
import com.vayne.model.model.RegisterRep;
import com.vayne.model.model.RegisterReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author : Yang Jian
 * @date : 2021/6/28 0028 22:33
 */
@RestController
@RequestMapping("/user")
public class UserInfoController implements UserInfoApi {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/register")
    @Override
    public RegisterRep register(@Valid RegisterReq registerReq) {
        return userInfoService.register(registerReq);
    }
}
