package org.study.trade.common.sequence;

import lombok.Getter;

/**
 * 生成序列用的id
 * @author Tomato
 * Created on 2021.08.27
 */
public class SequenceException extends Exception {

    @Getter
    private final String message;

    public SequenceException(String message) {
        super();
        this.message = message;
    }
}
