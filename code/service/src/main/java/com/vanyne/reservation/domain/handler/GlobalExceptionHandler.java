package com.vanyne.reservation.domain.handler;

import com.vanyne.reservation.domain.enums.CommonResult;
import com.vayne.model.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindException;
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
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result MyExceptionHandle(Exception exception) {
        log.error(exception.getMessage(), exception);
        BindingResult result = null;
        if (exception instanceof MethodArgumentNotValidException) {
            result = ((MethodArgumentNotValidException) exception).getBindingResult();
        } else if (exception instanceof BindException) {
            result = ((BindException) exception).getBindingResult();
        }

        if (result == null) {
            return CommonResult.FAILED.toResult();
        }

        StringBuilder errorMsg = new StringBuilder();
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(error ->
                    errorMsg.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("!"));
        }
        return new Result(CommonResult.FAILED.getCode(), errorMsg.toString());
    }

}
