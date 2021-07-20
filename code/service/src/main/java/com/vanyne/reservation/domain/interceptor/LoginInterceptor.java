package com.vanyne.reservation.domain.interceptor;

import com.vanyne.reservation.domain.annotations.UnLoginLimit;
import com.vanyne.reservation.domain.exception.UnLoginException;
import com.vanyne.reservation.infrastruction.common.ConstantType;
import com.vanyne.reservation.infrastruction.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : Yang Jian
 * @date : 2021/7/7 0007 22:24
 */
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UnLoginException {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            UnLoginLimit unlimited = method.getMethodAnnotation(UnLoginLimit.class);
            if (unlimited != null) {
                // 免登陆接口
                return true;
            } else {
                // 需要登录接口
                String token = request.getHeader("token");
                if (StringUtils.isEmpty(token)) {
                    throw new UnLoginException("token is null.");
                }

                Claims claimsByToken = JwtUtils.getClaimsByToken(token);
                if (claimsByToken == null) {
                    throw new UnLoginException("validate is token error.");
                }

                String user = stringRedisTemplate.opsForValue().get(ConstantType.TOKEN_KEY + token);
                if (StringUtils.isEmpty(user)) {
                    throw new UnLoginException("The token has expired. please login again.");
                }
            }
        }
        return true;
    }
}
