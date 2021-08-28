package org.study.trade.commodity.sequence;

import lombok.Getter;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 客户端分布式id缓存
 * @author Tomato
 * Created on 2021.08.26
 */
public class SequenceRange {

    /**
     * 可生成的最小的分布式id
     */
    @Getter
    private final long minId;

    /**
     * 可生成的最大的分布式id
     */
    @Getter
    private final long maxId;

    /**
     * 下一个待使用的分布式id
     */
    private final AtomicLong currentId;

    public SequenceRange(long minId, long maxId) {
        this.minId = minId;
        this.maxId = maxId;
        this.currentId = new AtomicLong(minId);
    }

    /**
     * 根据当前对象的分布式id范围，生成一个分布式id
     * @return 若越界，返回Optional.empty()
     */
    public Optional<Long> nextValue() {
        long nextValue = currentId.getAndIncrement();
        return nextValue > maxId ? Optional.empty() : Optional.of(nextValue);
    }
}
