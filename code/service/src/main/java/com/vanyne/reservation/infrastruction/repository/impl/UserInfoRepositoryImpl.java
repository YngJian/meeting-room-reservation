package com.vanyne.reservation.infrastruction.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vanyne.reservation.infrastruction.repository.UserInfoRepository;
import com.vanyne.reservation.infrastruction.repository.db.entity.UserInfoEntity;
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
    public int selectCountByUserName(String userName) {
        QueryWrapper<UserInfoEntity> userInfoWrapper = new QueryWrapper<>();
        userInfoWrapper.eq(UserInfoEntity.COL_USER_NAME, userName);
        return userInfoMapper.selectCount(userInfoWrapper);
    }

    /**
     * 根据用户名查询
     *
     * @param userName userName
     * @return int
     */
    @Override
    public UserInfoEntity selectUserByUserName(String userName) {
        QueryWrapper<UserInfoEntity> userInfoWrapper = new QueryWrapper<>();
        userInfoWrapper.eq(UserInfoEntity.COL_USER_NAME, userName);
        return userInfoMapper.selectOne(userInfoWrapper);
    }

    @Override
    public int selectByPhone(String phone) {
        QueryWrapper<UserInfoEntity> userInfoWrapper = new QueryWrapper<>();
        userInfoWrapper.eq(UserInfoEntity.COL_PHONE, phone);
        return userInfoMapper.selectCount(userInfoWrapper);
    }

    @Override
    public int selectByEmail(String email) {
        QueryWrapper<UserInfoEntity> userInfoWrapper = new QueryWrapper<>();
        userInfoWrapper.eq(UserInfoEntity.COL_EMAIL, email);
        return userInfoMapper.selectCount(userInfoWrapper);
    }

    /**
     * 修改密码
     *
     * @param userName       userName
     * @param userInfoEntity userInfoEntity
     * @return int
     */
    @Override
    public Integer updateByUserName(String userName, UserInfoEntity userInfoEntity) {
        QueryWrapper<UserInfoEntity> userInfoWrapper = new QueryWrapper<>();
        userInfoWrapper.eq(UserInfoEntity.COL_USER_NAME, userName);
        return userInfoMapper.update(userInfoEntity, userInfoWrapper);
    }

    /**
     * 修改状态
     *
     * @param userId         userId
     * @param userInfoEntity userInfoEntity
     * @return int
     */
    @Override
    public Integer updateByUserId(String userId, UserInfoEntity userInfoEntity) {
        QueryWrapper<UserInfoEntity> userInfoWrapper = new QueryWrapper<>();
        userInfoWrapper.eq(UserInfoEntity.COL_USER_ID, userId);
        return userInfoMapper.update(userInfoEntity, userInfoWrapper);
    }
}
