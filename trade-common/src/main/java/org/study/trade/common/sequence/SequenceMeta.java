package org.study.trade.common.sequence;

import lombok.Builder;

import java.lang.reflect.Field;

/**
 * 需要获取唯一序号的类的信息
 * @author Tomato
 * Created on 2021.08.26
 */
@Builder
public class SequenceMeta {

    /**
     * 需要获取唯一序号的类
     */
    private Class<?> clazz;

    /**
     * 唯一序号赋值到哪个成员变量
     */
    private Field autoSequenceField;

    /**
     * 序号生成
     */
    private SequenceGenerator sequenceGenerator;

    /**
     * 将SequenceGenerator生成的id通过反射注入到Object对象中
     * @param object 目标对象
     * @return true 操作成功
     */
    public boolean generateSequenceAndInject(Object object) {
        try {
            autoSequenceField.set(object, sequenceGenerator.nextValue());
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
}
