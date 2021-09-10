package org.study.trade.user;

import lombok.Getter;
import org.study.trade.common.error.CommonError;

/**
 * @author Tomato
 * Created on 2021.09.11
 */
@Getter
public enum ErrorEnum {
    /**
     * 未知错误
     */
    UNKNOWN_ERROR(10001, "unknown error"),
    /**
     * 没找到匹配的controller
     */
    NO_HANDLER_FOUND(10002, "no handler found"),
    /**
     * 参数错误
     */
    PARAMETER_ERROR(10003, "request parameter error"),

    /**
     * 注册请求错误
     */
    REGISTER_REQUEST_BODY_ERROR(20001, "invalid register request body");
    ;

    private final int code;
    private final String message;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonError create() {
        return new CommonError(this.code, this.message);
    }

    public CommonError create(String message) {
        return new CommonError(this.code, message);
    }
}
