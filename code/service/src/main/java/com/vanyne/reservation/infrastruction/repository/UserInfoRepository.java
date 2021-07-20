package com.vanyne.reservation.infrastruction.repository;

import com.vanyne.reservation.infrastruction.repository.db.entity.UserInfoEntity;

/**
 * @author : Yang Jian
 * @date : 2021/6/28 0028 22:51
 */
public interface UserInfoRepository {
    /**
     * 用户名是否唯一
     *
     * @param userName userName
     * @return int
     */
    int selectCountByUserName(String userName);

    /**
     * 根据用户名查询
     *
     * @param userName userName
     * @return int
     */
    UserInfoEntity selectUserByUserName(String userName);

    /**
     * 手机号是否唯一
     *
     * @param phone phone
     * @return int
     */
    int selectByPhone(String phone);

    /**
     * Email是否唯一
     *
     * @param email email
     * @return int
     */
    int selectByEmail(String email);

    /**
     * 修改密码
     *
     * @param userName       userName
     * @param userInfoEntity userInfoEntity
     * @return int
     */
    Integer updateByUserName(String userName, UserInfoEntity userInfoEntity);

    /**
     * 修改状态
     *
     * @param userId         userId
     * @param userInfoEntity userInfoEntity
     * @return int
     */
    Integer updateByUserId(String userId, UserInfoEntity userInfoEntity);

    /**
     * 根据userId查询
     *
     * @param userId userId
     * @return int
     */
    int selectCountByUserId(String userId);
}
