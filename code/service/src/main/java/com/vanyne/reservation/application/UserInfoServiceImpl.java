package com.vanyne.reservation.application;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanyne.reservation.application.impl.UserInfoService;
import com.vanyne.reservation.domain.enums.CommonResult;
import com.vanyne.reservation.infrastruction.repository.UserInfoRepository;
import com.vanyne.reservation.infrastruction.repository.db.entity.UserInfo;
import com.vanyne.reservation.infrastruction.repository.db.mapper.UserInfoMapper;
import com.vanyne.reservation.infrastruction.util.AesUtils;
import com.vayne.model.common.Result;
import com.vayne.model.model.RegisterRep;
import com.vayne.model.model.RegisterReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    private static final String AES_KEY = "vaynevaynevaynev";
    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public RegisterRep register(RegisterReq registerReq) {
        String userName = registerReq.getUserName();
        String password = registerReq.getPassword();
        String phone = registerReq.getPhone();
        String email = registerReq.getEmail();

        // 密码加密格式是否正确
        try {
            AesUtils.decode(AES_KEY, password);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new RegisterRep().setResult(new Result(CommonResult.INVALID_PARAM.getCode(),
                    "The password format is incorrect."));
        }

        // 用户名是否唯一
        int byUserName = userInfoRepository.selectByUserName(userName);
        if (byUserName > 0) {
            log.info("User name [{}] is duplicated.", userName);
            return new RegisterRep().setResult(new Result(CommonResult.INVALID_PARAM.getCode(), "User name is duplicated."));
        }

        // 手机号是否唯一
        int byPhone = userInfoRepository.selectByPhone(phone);
        if (byPhone > 0) {
            log.info("Phone [{}] is duplicated.", phone);
            return new RegisterRep().setResult(new Result(CommonResult.INVALID_PARAM.getCode(), "Phone is duplicated."));
        }

        // Email是否唯一
        int byEmail = userInfoRepository.selectByEmail(email);
        if (byEmail > 0) {
            log.info("Email [{}] is duplicated.", email);
            return new RegisterRep().setResult(new Result(CommonResult.INVALID_PARAM.getCode(), "Email is duplicated."));
        }

        // 插入用户
        UserInfo userInfo = getUserInfo(registerReq);
        this.save(userInfo);

        return new RegisterRep().setResult(CommonResult.RESULT_SUCCESS);
    }

    private UserInfo getUserInfo(RegisterReq registerReq) {
        UserInfo userInfo = new UserInfo();
        BeanUtil.copyProperties(registerReq, userInfo);
        userInfo.setUserId(UUID.randomUUID().toString().replaceAll("-", ""));
        return userInfo;
    }
}
