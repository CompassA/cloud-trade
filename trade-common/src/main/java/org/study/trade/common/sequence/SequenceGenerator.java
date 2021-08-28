package org.study.trade.common.sequence;

/**
 * 序号生成器
 * @author Tomato
 * Created on 2021.08.25
 */
public interface SequenceGenerator {

    /**
     * 序号生成器唯一标识
     * @return 字符串形式的唯一标识
     */
    String getSequenceId();

    /**
     * 生成唯一id
     * @return id
     * @throws SequenceException 序号生成异常
     */
    long nextValue() throws SequenceException;
}
