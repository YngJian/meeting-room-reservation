package com.vanyne.reservation.infrastruction.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author : Yang Jian
 * @date : 2021/7/4 0004 10:01
 */
public class JwtUtils {
    private static final long expire = 604800;
    // 秘钥
    private static final String secret = "VAYNeeXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9";

    /**
     * 创建一个token
     *
     * @param userId u
     * @return s
     */
    public static String generateToken(String userId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expire);
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 解析token
     */
    public static Claims getClaimsByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            System.out.println("validate is token error");
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
