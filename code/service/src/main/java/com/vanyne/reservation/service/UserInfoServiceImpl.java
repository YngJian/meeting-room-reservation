package com.vanyne.reservation.service;

import com.vanyne.reservation.repository.UserInfoRepository;
import com.vayne.model.model.RegisterRep;
import com.vayne.model.model.RegisterReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanyne.reservation.repository.db.mapper.UserInfoMapper;
import com.vanyne.reservation.repository.db.entity.UserInfo;
import com.vanyne.reservation.service.impl.UserInfoService;

import javax.validation.constraints.NotNull;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public RegisterRep register(RegisterReq registerReq) {
       String userName = registerReq.getUserName();
       String password = registerReq.getPassword();
       String phone = registerReq.getPhone();
       String email = registerReq.getEmail();

       // 用户名是否唯一
        int byUserName = userInfoRepository.selectByUserName(userName);
        // 手机号是否唯一
        int byPhone = userInfoRepository.selectByPhone(phone);
        // Email是否唯一
        int byEmail = userInfoRepository.selectByEmail(email);
        return null;
    }
}
