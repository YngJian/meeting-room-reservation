package com.vanyne.reservation.domain.enums;

import com.vayne.model.common.Result;

public enum CommonResult {
    SUCCESS(0, "success"),
    FAILED(1, "failed"),
    INVALID_PARAM(-1, "invalid param");

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

    public Result toResult() {
        return new Result(code, msg);
    }
}
