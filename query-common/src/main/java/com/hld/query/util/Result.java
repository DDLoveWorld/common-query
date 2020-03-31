package com.hld.query.util;


import com.hld.query.exception.ErrorCode;
import lombok.Data;
import org.springframework.validation.BindingResult;

import java.io.Serializable;

/**
 * Result
 *
 * @Author hld
 * @Date 2019-07-19
 */
@Data
public class Result<T> implements Serializable {

    private int code;
    private String message;
    private Object data;

    public Result() {
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code) {
        this.code = code;
    }

    /**
     * 普通成功返回
     *
     * @param data 获取的数据
     */
    public Result success(Object data) {

        this.code = ErrorCode.SUCCESS;
        this.message = "成功";
        this.data = data;
        return this;
    }

    /**
     * 普通成功返回
     */
    public Result success() {

        this.code = ErrorCode.SUCCESS;
        this.message = "成功";
        return this;
    }

    /**
     * 普通失败提示信息
     */
    public Result failed() {
        this.code = ErrorCode.FAILED;
        this.message = "失败";
        return this;
    }

    public Result failed(String msg) {
        this.code = ErrorCode.FAILED;
        this.message = msg;
        return this;
    }

    public Result failed(int code, String msg) {
        this.code = code;
        this.message = msg;
        return this;
    }

    /**
     * 参数验证失败使用
     *
     * @param message 错误信息
     */
    public Result validateFailed(String message) {
        this.code = ErrorCode.VALIDATE_FAILED;
        this.message = message;
        return this;
    }

    /**
     * 未登录时使用
     *
     * @param message 错误信息
     */
    public Result unauthorized(String message) {
        this.code = ErrorCode.TOKEN_INVALID;
        this.message = message;
        return this;
    }

    /**
     * 未授权时使用
     *
     * @param message 错误信息
     */
    public Result forbidden(String message) {
        this.code = ErrorCode.FORBIDDEN;
        this.message = message;
        return this;
    }

    /**
     * 参数验证失败使用
     *
     * @param result 错误信息
     */
    public Result validateFailed(BindingResult result) {
        validateFailed(result.getFieldError().getDefaultMessage());
        return this;
    }

    public Result<T> error(int code, String msg) {
        this.code = code;
        this.message = msg;
        return this;
    }

    public Result<T> error(int code) {
        this.code = code;
        return this;
    }

    public Result<T> error(String msg) {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.message = msg;
        return this;
    }


}
