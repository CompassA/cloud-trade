package org.study.trade.common.sequence;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在类的Long、long属性上加上此注解
 * 用SequenceGenerator为被标注的属性生成唯一id
 * @author Tomato
 * Created on 2021.08.25
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AutoSequence {

    /**
     * 表示要用哪个SequenceGenerator来生成id
     * @return SequenceGenerator的唯一标识 {@link SequenceGenerator#getSequenceId()}
     */
    String sequenceName();
}
