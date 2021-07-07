package com.vanyne.reservation.domain;

/**
 * @author : Yang Jian
 * @date : 2021/7/7 0007 22:37
 */
public class UnLoginException extends Exception {
    public UnLoginException() {
        super();
    }

    public UnLoginException(String message) {
        super(message);
    }
}
