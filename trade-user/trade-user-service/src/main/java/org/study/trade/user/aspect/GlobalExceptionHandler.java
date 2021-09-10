package org.study.trade.user.aspect;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.study.trade.common.error.BusinessException;
import org.study.trade.common.error.CommonError;
import org.study.trade.common.response.CommonResponse;
import org.study.trade.user.ErrorEnum;

/**
 * @author Tomato
 * Created on 2021.09.11
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResponse<CommonError> handle(Exception exception) {
        if (exception instanceof BusinessException) {
            return CommonResponse.fail(((BusinessException) exception).getError());
        }
        if (exception instanceof NoHandlerFoundException) {
            return CommonResponse.fail(ErrorEnum.NO_HANDLER_FOUND.create());
        }
        if (exception instanceof ServletRequestBindingException) {
            return CommonResponse.fail(ErrorEnum.PARAMETER_ERROR.create());
        }
        return CommonResponse.fail(ErrorEnum.UNKNOWN_ERROR.create());
    }
}
