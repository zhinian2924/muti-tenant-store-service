package com.example.storesaas.platform.web;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.example.storesaas.platform.error.BusinessException;
import com.example.storesaas.platform.error.ResultCode;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusiness(BusinessException ex) {
        return ApiResponse.fail(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(NotLoginException.class)
    public ApiResponse<Void> handleNotLogin(NotLoginException ex) {
        return ApiResponse.fail(ResultCode.UNAUTHORIZED, "请先登录");
    }

    @ExceptionHandler(NotPermissionException.class)
    public ApiResponse<Void> handleNoPermission(NotPermissionException ex) {
        return ApiResponse.fail(ResultCode.FORBIDDEN, "无权限访问");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ApiResponse<Void> handleValidation(Exception ex) {
        String details;
        if (ex instanceof MethodArgumentNotValidException validationException) {
            details = validationException.getBindingResult().getFieldErrors().stream()
                    .map(error -> error.getField() + ":" + error.getDefaultMessage())
                    .collect(Collectors.joining("，"));
        } else if (ex instanceof BindException bindException) {
            details = bindException.getBindingResult().getFieldErrors().stream()
                    .map(error -> error.getField() + ":" + error.getDefaultMessage())
                    .collect(Collectors.joining("，"));
        } else {
            details = "";
        }
        return ApiResponse.fail(ResultCode.VALIDATION_ERROR,
                details.isBlank() ? "参数校验失败" : "参数校验失败：" + details);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ApiResponse<Void> handleDuplicateEntry(SQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage();
        if (message != null && message.contains("Duplicate entry")) {
            return ApiResponse.fail(ResultCode.CONFLICT, "数据已存在，请勿重复添加");
        }
        return ApiResponse.fail(ResultCode.CONFLICT, "数据冲突");
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleUnknown(Exception ex) {
        log.error("未处理的请求异常", ex);
        return ApiResponse.fail(ResultCode.INTERNAL_ERROR, "系统异常");
    }
}
