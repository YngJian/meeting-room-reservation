package com.vanyne.reservation.infrastruction.repository;

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
    int selectByUserName(String userName);

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
}
