package com.vanyne.reservation.infrastruction.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vanyne.reservation.infrastruction.repository.UserInfoRepository;
import com.vanyne.reservation.infrastruction.repository.db.entity.UserInfo;
import com.vanyne.reservation.infrastruction.repository.db.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author : Yang Jian
 * @date : 2021/6/28 0028 22:51
 */
@Repository
public class UserInfoRepositoryImpl implements UserInfoRepository {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public int selectByUserName(String userName) {
        QueryWrapper<UserInfo> userInfoWrapper = new QueryWrapper<>();
        userInfoWrapper.eq(UserInfo.COL_USER_NAME, userName);
        return userInfoMapper.selectCount(userInfoWrapper);
    }

    @Override
    public int selectByPhone(String phone) {
        QueryWrapper<UserInfo> userInfoWrapper = new QueryWrapper<>();
        userInfoWrapper.eq(UserInfo.COL_PHONE, phone);
        return userInfoMapper.selectCount(userInfoWrapper);
    }

    @Override
    public int selectByEmail(String email) {
        QueryWrapper<UserInfo> userInfoWrapper = new QueryWrapper<>();
        userInfoWrapper.eq(UserInfo.COL_EMAIL, email);
        return userInfoMapper.selectCount(userInfoWrapper);
    }
}
