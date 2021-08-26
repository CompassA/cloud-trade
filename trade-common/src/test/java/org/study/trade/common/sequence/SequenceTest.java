package org.study.trade.common.sequence;

import lombok.Getter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomato
 * Created on 2021.08.26
 */
public class SequenceTest {

    private static final String mockName = "mock";

    private static final long mockValue = 234;

    private SequenceProcessor processor;

    @Before
    public void init() {
        Map<String, SequenceGenerator> sequenceMap = new HashMap<>();
        MockSequence mockSequence = new MockSequence();
        sequenceMap.put(mockSequence.getSequenceId(), mockSequence);
        this.processor = new SequenceProcessor(sequenceMap);
    }

    /**
     * 测试目标对象类型为long的情况
     */
    @Test
    public void testLong() {
        Object1 object1 = new Object1();
        Assert.assertTrue(processor.injectAutoId(object1));
        Assert.assertEquals(mockValue, object1.getId());
    }

    /**
     * 检测目标对象类型为Long的情况
     */
    @Test
    public void testLongWrapper() {
        Object2 object2 = new Object2();
        Assert.assertTrue(processor.injectAutoId(object2));
        Assert.assertEquals(mockValue, (long) object2.getId());
    }

    /**
     * 测试类型不匹配的情况
     */
    @Test
    public void testInvalidType() {
        Object3 object3 = new Object3();
        Assert.assertFalse(processor.injectAutoId(object3));
    }

    /**
     * 测试没有AutoSequence的情况
     */
    @Test
    public void testNoAnnotation() {
        Object4 object4 = new Object4();
        Assert.assertFalse(processor.injectAutoId(object4));
    }

    /**
     * 测试找不到SequenceGenerator的情况
     */
    @Test
    public void testGeneratorNotFound() {
        Object5 object5 = new Object5();
        Assert.assertFalse(processor.injectAutoId(object5));
    }

    public static class MockSequence implements SequenceGenerator {
        @Override
        public String getSequenceId() {
            return mockName;
        }
        @Override
        public long nextValue() {
            return mockValue;
        }
    }

    @Getter
    public static class Object1 {
        @AutoSequence(sequenceName = mockName)
        private long id;
    }

    @Getter
    public static class Object2 {
        @AutoSequence(sequenceName = mockName)
        private Long id;
    }

    @Getter
    public static class Object3 {
        @AutoSequence(sequenceName = mockName)
        private String id;
    }

    public static class Object4 {
        private long id1;
        private long id2;
    }

    @Getter
    public static class Object5 {
        @AutoSequence(sequenceName = "not found")
        private long id;
    }
}
