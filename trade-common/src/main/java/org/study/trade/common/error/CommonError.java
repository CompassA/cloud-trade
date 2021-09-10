package org.study.trade.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tomato
 * Created on 2021.09.11
 */
@Getter
@AllArgsConstructor
public class CommonError {
    private int code;
    private String message;
}
