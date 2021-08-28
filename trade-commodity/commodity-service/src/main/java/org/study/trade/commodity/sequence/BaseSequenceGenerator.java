package org.study.trade.commodity.sequence;

import org.study.trade.common.sequence.SequenceException;
import org.study.trade.common.sequence.SequenceGenerator;

import java.util.Optional;

/**
 * 获取分布式id的基础实现
 * @author Tomato
 * Created on 2021.08.26
 */
public abstract class BaseSequenceGenerator implements SequenceGenerator {

    /**
     * 当前客户端缓冲在内存中的分布式id范围
     */
    protected volatile SequenceRange range;

    @Override
    public long nextValue() throws SequenceException {
        // 初始化内存的分布式id，双检锁检测是否别的线程已经更新成功
        if (range == null) {
            synchronized (this) {
                if (range == null) {
                    range = fetchSequence()
                            .orElseThrow(() -> new SequenceException("init sequence range failed"));
                }
            }
        }

        // 若正常获取，直接返回
        Optional<Long> value = range.nextValue();
        if (value.isPresent()) {
            return value.get();
        }

        // 获取失败，更新序列, 双检锁检测是否别的线程已经更新成功
        synchronized (this) {
            value = range.nextValue();
            if (value.isPresent()) {
                return value.get();
            }

            // 高并发情况下可能出现，别的线程将当前线程更新的range瞬间消费完的情况, 故不断重试
            for (int i = 0; i < 100000000; ++i) {
                range = fetchSequence()
                        .orElseThrow(() -> new SequenceException("update sequence range failed"));
                value = range.nextValue();
                if (value.isPresent()) {
                    return value.get();
                }
            }
        }

        // 没法再取到分布式id了，抛异常
        throw new SequenceException("no more sequence");
    }

    /**
     * 内存中缓存的分布式id全部取完了，更新号段
     * @return 更新后的号段
     */
    protected abstract Optional<SequenceRange> fetchSequence();
}
