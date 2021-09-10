package org.study.trade.common.error;

import lombok.Getter;

/**
 * @author Tomato
 * Created on 2021.09.11
 */
@Getter
public class BusinessException extends Exception {

    private final CommonError error;

    public BusinessException(CommonError error) {
        super();
        this.error = error;
    }
}
