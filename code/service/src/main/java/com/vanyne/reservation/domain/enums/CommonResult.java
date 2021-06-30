package com.vanyne.reservation.domain.enums;

import com.vayne.model.common.Result;

public enum CommonResult {
    SUCCESS(0, "success"),
    FAILED(1, "failed"),
    INVALID_PARAM(-1, "success");

    public static final Result RESULT_SUCCESS = new Result(SUCCESS.getCode(), SUCCESS.getMsg());
    public static final Result RESULT_FAILED = new Result(FAILED.getCode(), FAILED.getMsg());
    public static final Result RESULT_INVALID_PARAM = new Result(INVALID_PARAM.getCode(), INVALID_PARAM.getMsg());

    private final int code;
    private final String msg;

    CommonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
