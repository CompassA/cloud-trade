package org.study.trade.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Tomato
 * Created on 2021.09.11
 */
@Getter
@AllArgsConstructor
public class CommonResponse<T> {

    private static final String SUCCESS_STATUS = "success";
    private static final String FAILURE_STATUS = "failed";

    private String status;

    private T data;

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(SUCCESS_STATUS, data);
    }

    public static <T> CommonResponse<T> fail(T data) {
        return new CommonResponse<>(FAILURE_STATUS, data);
    }
}
