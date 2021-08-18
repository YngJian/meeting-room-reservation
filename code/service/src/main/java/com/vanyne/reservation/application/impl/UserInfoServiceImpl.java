package com.vanyne.reservation.application.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vanyne.reservation.application.UserInfoService;
import com.vanyne.reservation.domain.enums.CommonResult;
import com.vanyne.reservation.infrastruction.common.ConstantType;
import com.vanyne.reservation.infrastruction.common.StatusType;
import com.vanyne.reservation.infrastruction.repository.UserInfoRepository;
import com.vanyne.reservation.infrastruction.repository.db.entity.UserInfoEntity;
import com.vanyne.reservation.infrastruction.repository.db.mapper.UserInfoMapper;
import com.vanyne.reservation.infrastruction.util.AesUtils;
import com.vanyne.reservation.infrastruction.util.CommonUtils;
import com.vanyne.reservation.infrastruction.util.JwtUtils;
import com.vayne.model.common.Result;
import com.vayne.model.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoEntity> implements UserInfoService {
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
            AesUtils.decode(ConstantType.AES_KEY, password);
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
        BeanUtils.copyProperties(registerReq, userInfoEntity);
        userInfoEntity.setUserId(CommonUtils.getUUID());
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
        if (!StatusType.NORMAL.equals(userInfoEntity.getStatus())) {
            log.info("Account is not normal. username: [{}].", userName);
            return new LoginRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(), "Account is not normal."));
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
        BeanUtils.copyProperties(userInfoEntity, userInfo);

        // 生成token
        String token = JwtUtils.generateToken(userInfoEntity.getUserId());

        // 设置redis
        stringRedisTemplate.opsForValue().set(ConstantType.TOKEN_KEY + token,
                JSONUtil.toJsonStr(userInfo), ConstantType.TOKEN_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 删除之前错误次数
        stringRedisTemplate.delete(ConstantType.PWD_WRONG_KEY + userInfoEntity.getUserId());

        return new LoginRep()
                .setResult(CommonResult.SUCCESS.toResult())
                .setToken(token)
                .setUserInfo(userInfo);
    }

    private LoginRep pwdWrongRep(String userName, UserInfoEntity userInfoEntity) {
        Long wrongTimes = stringRedisTemplate.opsForValue().increment(ConstantType.PWD_WRONG_KEY + userInfoEntity.getUserId());
        if (wrongTimes != null && wrongTimes < ConstantType.PWD_MAX_RETRY_TIME) {
            // 返回剩余错误次数
            int remainTimes = (int) (ConstantType.PWD_MAX_RETRY_TIME - wrongTimes);
            stringRedisTemplate.opsForValue().set(ConstantType.PWD_WRONG_KEY + userInfoEntity.getUserId(),
                    String.valueOf(wrongTimes), ConstantType.PWD_WRONG_EXPIRE_HOURS, TimeUnit.HOURS);
            log.info("Incorrect password. username: [{}]. remaining times：[{}].", userName, remainTimes);
            return new LoginRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    String.format("Incorrect password. %s chances left.", remainTimes)));
        } else {
            // 锁定账户
            UserInfoEntity infoEntity = UserInfoEntity.builder()
                    .id(userInfoEntity.getId())
                    .status(StatusType.DISABLE)
                    .build();
            boolean updateById = this.updateById(infoEntity);

            // 锁定成功设置redis自动解锁过期键
            if (updateById) {
                stringRedisTemplate.opsForValue().set(ConstantType.PWD_AUTO_UNLOCK_KEY + userInfoEntity.getUserId(),
                        userInfoEntity.getUserId(), ConstantType.PWD_WRONG_EXPIRE_HOURS, TimeUnit.HOURS);
            }

            log.info("Account locked，username: [{}].", userName);
            return new LoginRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(), "Account locked，"));
        }
    }

    /**
     * 修改密码
     *
     * @param modifyPwdReq modifyPwdReq
     * @param token        token
     * @return r
     */
    @Override
    public ModifyPwdRep modifyPwd(ModifyPwdReq modifyPwdReq, String token) {
        String user = stringRedisTemplate.opsForValue().get(ConstantType.TOKEN_KEY + token);
        if (StringUtils.isEmpty(user)) {
            log.info("The token [{}] has expired please log in again", token);
            return new ModifyPwdRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(), "The token has expired, please log in again!"));
        }

        // 密码加密格式是否正确
        String newPwd = modifyPwdReq.getNewPwd();
        try {
            AesUtils.decode(ConstantType.AES_KEY, newPwd);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ModifyPwdRep()
                    .setResult(new Result(CommonResult.INVALID_PARAM.getCode(), "The password format is incorrect."));
        }

        UserInfo userInfo = JSON.parseObject(user, UserInfo.class);
        String userName = userInfo.getUserName();

        // 用户名是否正确
        UserInfoEntity userInfoEntity = userInfoRepository.selectUserByUserName(userName);
        if (userInfoEntity == null) {
            log.info("Username does not exist. token: [{}]", token);
            return new ModifyPwdRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(), "Username does not exist."));
        }

        // 与旧密码是否相同
        String password = userInfoEntity.getPassword();
        if (password.equals(newPwd)) {
            log.info("The new password is the same as the old password,token:[{}]", token);
            return new ModifyPwdRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(), "The new password is the same as the old password."));
        }

        // 修改密码
        UserInfoEntity infoEntity = UserInfoEntity.builder()
                .password(newPwd)
                .build();
        Integer integer = userInfoRepository.updateByUserName(userName, infoEntity);
        if (integer == 0) {
            log.info("Failed to change the password.token:[{}]", token);
            return new ModifyPwdRep()
                    .setResult(
                            new Result(CommonResult.FAILED.getCode(), "Failed to change the password."));
        }

        return new ModifyPwdRep()
                .setResult(CommonResult.SUCCESS.toResult());
    }

    /**
     * 解锁账户
     *
     * @param unlockReq unlockReq
     * @return r
     */
    @Override
    public UnlockRep unlockUser(@Valid UnlockReq unlockReq) {
        String userName = unlockReq.getUserName();

        // 用户名是否存在
        UserInfoEntity infoEntity = userInfoRepository.selectUserByUserName(userName);
        if (infoEntity == null) {
            log.info("User name [{}] is not exist.", userName);
            return new UnlockRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(), "User name is not exist."));
        }

        // 解锁
        UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                .status(StatusType.NORMAL)
                .build();
        Integer integer = userInfoRepository.updateByUserName(userName, userInfoEntity);
        if (integer == 0) {
            log.info("Unlock failed. user name [{}].", userName);
            return new UnlockRep()
                    .setResult(
                            new Result(CommonResult.FAILED.getCode(), "Unlock failed."));
        }

        // 删除redis密码错误次数
        stringRedisTemplate.delete(ConstantType.PWD_WRONG_KEY + infoEntity.getUserId());
        stringRedisTemplate.delete(ConstantType.PWD_AUTO_UNLOCK_KEY + infoEntity.getUserId());

        return new UnlockRep()
                .setResult(CommonResult.SUCCESS.toResult());
    }

    @Override
    public LoginRep getLoginInfo(String token) {
        String user = stringRedisTemplate.opsForValue().get(ConstantType.TOKEN_KEY + token);
        if (StringUtils.isEmpty(user)) {
            log.info("The token [{}] has expired, please log in again!", token);
            return new LoginRep()
                    .setResult(
                            new Result(CommonResult.INVALID_PARAM.getCode(),
                                    "The token has expired, please log in again!")
                    );
        }
        UserInfo userInfo = JSONUtil.toBean(user, UserInfo.class);
        return new LoginRep()
                .setResult(CommonResult.SUCCESS.toResult())
                .setUserInfo(userInfo);
    }
}
