/**
 * Copyright (c) 2018 北航天华 All rights reserved.
 * <p>
 * https://www.techcomer.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.hld.query.exception;

/**
 * 错误编码，由5位数字组成，前2位为模块编码，后3位为业务编码
 * <p>
 * 如：10001（10代表系统模块，001代表业务代码）
 * </p>
 *
 * @author huald
 * @version 1.0.0
 * @email 869701411@qq.com
 */
public interface ErrorCode {
    /**
     * 成功
     */
    int SUCCESS = 0;
    /**
     * 操作失败
     */
    int FAILED = -1;
    /**
     * 参数校验失败
     */
    int VALIDATE_FAILED = 3;
    /**
     * 服务器内部异常
     */
    int INTERNAL_SERVER_ERROR = 500;
    /**
     * 拒绝访问，没有权限
     */
    int FORBIDDEN = 403;
    /**
     * 不能为空
     */
    int NOT_NULL = 10001;
    /**
     * 获取参数失败
     */
    int PARAMS_GET_ERROR = 10003;

    /**
     * token失效，请重新登录
     */
    int TOKEN_INVALID = 10021;

    /**
     * 不能包含非法字符
     */
    int INVALID_SYMBOL = 10029;
    /**
     * 参数格式不正确，请使用JSON格式
     */
    int JSON_FORMAT_ERROR = 10030;

}
