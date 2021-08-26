package org.study.trade.common.sequence;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 为标注了 {@link AutoSequence} 属性注入值
 * @author Tomato
 * Created on 2021.08.25
 */
public class SequenceProcessor {

    /**
     * 需要获取自动增长id的类 -> 自动增长的相关信息
     */
    private final ConcurrentMap<Class<?>, SequenceMeta> autoIdInfo = new ConcurrentHashMap<>(0);

    /**
     * id生成器名字 -> id生成器
     */
    private final Map<String, SequenceGenerator> sequenceMap;

    public SequenceProcessor(Map<String, SequenceGenerator> sequenceMap) {
        this.sequenceMap = sequenceMap;
    }

    /**
     * 根据类的配置信息，调用id生成器，将生成的id注入对象中
     * @param object 需要生成id的对象
     * @return true 生成id成功
     */
    public boolean injectAutoId(Object object) {
        SequenceMeta sequenceMeta = getSequenceMeta(object);
        if (sequenceMeta == null) {
            return false;
        }
        return sequenceMeta.generateSequenceAndInject(object);
    }

    private SequenceMeta getSequenceMeta(Object object) {
        return autoIdInfo.computeIfAbsent(object.getClass(), key -> {
            for (Field field : key.getDeclaredFields()) {
                Class<?> fieldType = field.getType();
                if ((!Long.class.isAssignableFrom(fieldType)
                        && !long.class.isAssignableFrom(fieldType))
                        || !field.isAnnotationPresent(AutoSequence.class)) {
                    continue;
                }
                AutoSequence annotation = field.getAnnotation(AutoSequence.class);
                SequenceGenerator generator = this.sequenceMap.get(annotation.sequenceName());
                if (generator == null) {
                    continue;
                }
                field.setAccessible(true);
                return SequenceMeta.builder()
                        .clazz(key)
                        .sequenceGenerator(generator)
                        .autoSequenceField(field)
                        .build();

            }
            return null;
        });
    }
}
