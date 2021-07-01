package com.vanyne.reservation.domain.handler;

import com.vanyne.reservation.domain.enums.CommonResult;
import com.vayne.model.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author : Yang Jian
 * @date : 2021/6/30 0030 21:46
 */
@Configuration
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Result MyExceptionHandle(MethodArgumentNotValidException exception) {
        exception.printStackTrace();
        BindingResult result = exception.getBindingResult();
        StringBuilder errorMsg = new StringBuilder();

        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(error -> {
                errorMsg.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("!");
            });
        }
        return new Result(CommonResult.FAILED.getCode(), errorMsg.toString());
    }

    /**
     * 处理 Exception 异常
     *
     * @param e 异常
     * @return r
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return CommonResult.FAILED.toResult();
    }
}
