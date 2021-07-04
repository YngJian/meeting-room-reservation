package com.vanyne.reservation.infrastruction.common;

/**
 * @author : Yang Jian
 * @date : 2021/7/4 0004 17:40
 */
public class ConstantType {
    public static final String AES_KEY = "vaynevaynevaynev";

    public static final Integer PWD_MAX_RETRY_TIME = 5;

    public static final String PWD_WRONG_KEY = "reservation:pwd:retry:";

    public static final String PWD_AUTO_UNLOCK_KEY = "reservation:pwd:unlock:";

    public static final String AUTO_UNLOCK_REDIS_KEY = "reservation:auto:unlock:";

    public static final String TOKEN_KEY = "token:";

    public static final long TOKEN_EXPIRE_MINUTES = 30;

    public static final long PWD_WRONG_EXPIRE_HOURS = 24;

    public static final long AUTO_UNLOCK_REDIS_EXPIRE_HOURS = 24;
}
