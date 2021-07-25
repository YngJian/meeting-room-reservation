package com.vanyne.reservation.infrastruction.util;

import com.vanyne.reservation.infrastruction.common.ConstantType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author : Yang Jian
 * @date : 2021/7/4 0004 10:01
 */
@Slf4j
public class JwtUtils {
    /**
     * 创建一个token
     *
     * @param userId u
     * @return s
     */
    public static String generateToken(String userId) {
        Date date = new Date();
        LocalDateTime localDateTime = DateUtils.dateToLocalDateTime(date).plusMinutes(ConstantType.TOKEN_EXPIRE_MINUTES);
        Date expirationDate = DateUtils.localDateTimeToDate(localDateTime);
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setSubject(userId)
                .setIssuedAt(date)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, ConstantType.JWT_SECRET)
                .compact();
    }

    /**
     * 解析token
     */
    public static Claims getClaimsByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(ConstantType.JWT_SECRET)
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("validate is token error", e);
            return null;
        }
    }

    /**
     * 判断 token 是否过期
     */
    public static boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    public static void main(String[] args) {
        String generateToken = generateToken("123");
        System.out.println(generateToken);

        Claims claimsByToken = getClaimsByToken(generateToken);
        System.out.println(claimsByToken.toString());
    }
}
