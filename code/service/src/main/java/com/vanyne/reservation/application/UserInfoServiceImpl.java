package com.vanyne.reservation.application;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanyne.reservation.application.impl.UserInfoService;
import com.vanyne.reservation.domain.enums.CommonResult;
import com.vanyne.reservation.infrastruction.common.UserStatusType;
import com.vanyne.reservation.infrastruction.repository.UserInfoRepository;
import com.vanyne.reservation.infrastruction.repository.db.entity.UserInfoEntity;
import com.vanyne.reservation.infrastruction.repository.db.mapper.UserInfoMapper;
import com.vanyne.reservation.infrastruction.util.AesUtils;
import com.vanyne.reservation.infrastruction.util.JwtUtils;
import com.vayne.model.common.Result;
import com.vayne.model.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoEntity> implements UserInfoService {
    private static final String AES_KEY = "vaynevaynevaynev";

    private static final Integer PWD_MAX_RETRY_TIME = 5;

    private static final String PWD_WRONG_KEY = "reservation:pwd:retry";

    private static final String PWD_AUTO_UNLOCK_KEY = "reservation:pwd:unlock";

    private static final String TOKEN_KEY = "token:";

    private static final long TOKEN_EXPIRE_MINUTES = 30;

    private static final long PWD_WRONG_EXPIRE_HOURS = 24;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
            return new RegisterRep()
                    .setResult(new Result(CommonResult.INVALID_PARAM.getCode(), "The password format is incorrect."));
        }

        // 用户名是否唯一
        int byUserName = userInfoRepository.selectCountByUserName(userName);
        if (byUserName > 0) {
            log.info("User name [{}] is duplicated.", userName);
            return new RegisterRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(), "User name is duplicated."));
        }

        // 手机号是否唯一
        int byPhone = userInfoRepository.selectByPhone(phone);
        if (byPhone > 0) {
            log.info("Phone [{}] is duplicated.", phone);
            return new RegisterRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(), "Phone is duplicated."));
        }

        // Email是否唯一
        int byEmail = userInfoRepository.selectByEmail(email);
        if (byEmail > 0) {
            log.info("Email [{}] is duplicated.", email);
            return new RegisterRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(), "Email is duplicated."));
        }

        // 插入用户
        UserInfoEntity userInfoEntity = getUserInfo(registerReq);
        this.save(userInfoEntity);

        return new RegisterRep()
                .setResult(CommonResult.SUCCESS.toResult());
    }

    private UserInfoEntity getUserInfo(RegisterReq registerReq) {
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        BeanUtil.copyProperties(registerReq, userInfoEntity);
        userInfoEntity.setUserId(UUID.randomUUID().toString().replaceAll("-", ""));
        return userInfoEntity;
    }

    /**
     * 用户名是否唯一
     *
     * @param userName userName
     * @return r
     */
    @Override
    public RepeatRep userNameRepeated(String userName) {
        // 用户名是否唯一
        int byUserName = userInfoRepository.selectCountByUserName(userName);
        if (byUserName > 0) {
            log.info("User name [{}] is duplicated.", userName);
            return new RepeatRep()
                    .setResult(CommonResult.SUCCESS.toResult())
                    .setRepeated(true);
        } else {
            return new RepeatRep()
                    .setResult(CommonResult.SUCCESS.toResult())
                    .setRepeated(false);
        }
    }

    /**
     * 手机号是否唯一
     *
     * @param phone phone
     * @return r
     */
    @Override
    public RepeatRep phoneRepeated(String phone) {
        // 手机号是否唯一
        int byPhone = userInfoRepository.selectByPhone(phone);
        if (byPhone > 0) {
            log.info("Phone [{}] is duplicated.", phone);
            return new RepeatRep()
                    .setResult(CommonResult.SUCCESS.toResult())
                    .setRepeated(true);
        } else {
            return new RepeatRep()
                    .setResult(CommonResult.SUCCESS.toResult())
                    .setRepeated(false);
        }
    }

    /**
     * 邮箱是否唯一
     *
     * @param email email
     * @return r
     */
    @Override
    public RepeatRep emailRepeated(String email) {
        // Email是否唯一
        int byEmail = userInfoRepository.selectByEmail(email);
        if (byEmail > 0) {
            log.info("Email [{}] is duplicated.", email);
            return new RepeatRep()
                    .setResult(CommonResult.SUCCESS.toResult())
                    .setRepeated(true);
        } else {
            return new RepeatRep()
                    .setResult(CommonResult.SUCCESS.toResult())
                    .setRepeated(false);
        }
    }

    /**
     * 登录
     *
     * @param loginReq l
     * @return r
     */
    @Override
    public LoginRep login(LoginReq loginReq) {
        String userName = loginReq.getUserName();
        String password = loginReq.getPassword();

        // 用户名是否正确
        UserInfoEntity userInfoEntity = userInfoRepository.selectUserByUserName(userName);
        if (userInfoEntity == null) {
            log.info("Username [{}] does not exist.", userName);
            return new LoginRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(), "Username does not exist."));
        }

        // 账户是否正常
        if (!UserStatusType.NORMAL.equals(userInfoEntity.getStatus())) {
            log.info("Account is not normal. username: [{}].", userName);
            return new LoginRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(), "Account is not normal."))
                    .setLocked(true);
        }

        // 密码是否正确
        if (!password.equals(userInfoEntity.getPassword())) {
            return this.pwdWrongRep(userName, userInfoEntity);
        } else {
            return this.pwdRightRep(userInfoEntity);
        }
    }

    private LoginRep pwdRightRep(UserInfoEntity userInfoEntity) {
        UserInfo userInfo = new UserInfo();
        BeanUtil.copyProperties(userInfoEntity, userInfo);

        // 生成token
        String token = JwtUtils.generateToken(userInfoEntity.getUserId());

        // 设置redis
        stringRedisTemplate.opsForValue().set(TOKEN_KEY + token,
                JSONUtil.toJsonStr(userInfo), TOKEN_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 删除之前错误次数
        stringRedisTemplate.delete(PWD_WRONG_KEY + ":" + userInfoEntity.getUserId());

        return new LoginRep()
                .setResult(CommonResult.SUCCESS.toResult())
                .setToken(token)
                .setUserInfo(userInfo);
    }

    private LoginRep pwdWrongRep(String userName, UserInfoEntity userInfoEntity) {
        Long wrongTimes = stringRedisTemplate.opsForValue().increment(PWD_WRONG_KEY + ":" + userInfoEntity.getUserId());
        if (wrongTimes != null && wrongTimes < PWD_MAX_RETRY_TIME) {
            // 返回剩余错误次数
            int remainTimes = (int) (PWD_MAX_RETRY_TIME - wrongTimes);
            stringRedisTemplate.opsForValue().set(PWD_WRONG_KEY + ":" + userInfoEntity.getUserId(),
                    String.valueOf(wrongTimes), PWD_WRONG_EXPIRE_HOURS, TimeUnit.HOURS);
            log.info("Incorrect password. username: [{}]. remaining times：[{}].", userName, remainTimes);
            return new LoginRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(), "Incorrect password."))
                    .setRemainTimes(remainTimes)
                    .setPwdWrong(true);
        } else {
            // 锁定账户
            UserInfoEntity infoEntity = UserInfoEntity.builder()
                    .id(userInfoEntity.getId())
                    .status(UserStatusType.DISABLE)
                    .build();
            boolean updateById = this.updateById(infoEntity);

            // 锁定成功设置redis自动解锁过期键
            if (updateById) {
                stringRedisTemplate.opsForValue().set(PWD_AUTO_UNLOCK_KEY + ":" + userInfoEntity.getUserId(),
                        userInfoEntity.getUserId(), PWD_WRONG_EXPIRE_HOURS, TimeUnit.HOURS);
            }

            log.info("Account locked，username: [{}].", userName);
            return new LoginRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(), "Account locked，"))
                    .setLocked(true);
        }
    }
}
