package com.hld.query.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 *
 * @author huald
 * @version 1.0.0
 * @email 869701411@qq.com
 * @date 2019/1/10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommonException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;


    public CommonException(int code, String message) {
        this.code = code;
        this.msg = message;
    }


    public CommonException(int code, Throwable e) {
        super(e);
        this.code = code;
    }

    public CommonException(int code, Throwable e, String message) {
        super(e);
        this.code = code;
        this.msg = message;
    }

    public CommonException(String msg) {
        super(msg);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
    }

    public CommonException(int code) {
        this.code = code;
    }

    public CommonException(String msg, Throwable e) {
        super(msg, e);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
    }

    public CommonException(String msg, int code) {
        this.code = code;
        this.msg = msg;
    }

}