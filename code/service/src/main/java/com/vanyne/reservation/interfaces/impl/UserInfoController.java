package com.vanyne.reservation.interfaces.impl;

import com.vanyne.reservation.application.impl.UserInfoService;
import com.vayne.model.api.UserInfoApi;
import com.vayne.model.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author : Yang Jian
 * @date : 2021/6/28 0028 22:33
 */
@RestController
@RequestMapping("/user")
public class UserInfoController implements UserInfoApi {
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 注册
     *
     * @param registerReq r
     * @return r
     */
    @PostMapping("/register")
    @Override
    public RegisterRep register(@RequestBody @Valid RegisterReq registerReq) {
        return userInfoService.register(registerReq);
    }

    /**
     * 用户名是否唯一
     *
     * @param userName userName
     * @return r
     */
    @GetMapping("/name/{userName}")
    @Override
    public RepeatRep userNameRepeated(@PathVariable @NotNull String userName) {
        return userInfoService.userNameRepeated(userName);
    }

    /**
     * 手机号是否唯一
     *
     * @param phone phone
     * @return r
     */
    @GetMapping("/phone/{phone}")
    @Override
    public RepeatRep phoneRepeated(@PathVariable @NotNull String phone) {
        return userInfoService.phoneRepeated(phone);
    }

    /**
     * 邮箱是否唯一
     *
     * @param email email
     * @return r
     */
    @GetMapping("/email/{email}")
    @Override
    public RepeatRep emailRepeated(@PathVariable @NotNull String email) {
        return userInfoService.emailRepeated(email);
    }

    /**
     * 登录
     *
     * @param loginReq r
     * @return r
     */
    @PostMapping("/login")
    @Override
    public LoginRep login(@RequestBody @Valid @NotNull LoginReq loginReq) {
        return userInfoService.login(loginReq);
    }

    /**
     * 修改密码
     *
     * @param modifyPwdReq modifyPwdReq
     * @return r
     */
    @PutMapping("/change/password")
    @Override
    public ModifyPwdRep modifyPwd(@Valid @NotNull @RequestBody ModifyPwdReq modifyPwdReq,
                                  @RequestHeader(value = "token") String token) {
        return userInfoService.modifyPwd(modifyPwdReq, token);
    }

    /**
     * 解锁用户
     *
     * @param unlockReq unlockReq
     * @return r
     */
    @PutMapping("/unlock")
    @Override
    public UnlockRep unlockUser(@RequestBody @Valid UnlockReq unlockReq) {
        return userInfoService.unlockUser(unlockReq);
    }
}
