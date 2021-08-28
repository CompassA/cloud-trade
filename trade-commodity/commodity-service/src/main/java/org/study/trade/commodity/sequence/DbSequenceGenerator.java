package org.study.trade.commodity.sequence;

import lombok.NoArgsConstructor;
import org.study.trade.commodity.mapper.data.SequencePO;

import java.util.Optional;

/**
 * 通过数据库获取分布式id
 * @author Tomato
 * Created on 2021.08.26
 */
@NoArgsConstructor
public class DbSequenceGenerator extends BaseSequenceGenerator {

    private int dbSequenceKey;

    private SequencePOMapper mapper;

    public DbSequenceGenerator(int dbSequenceKey, SequencePOMapper mapper) {
        this.dbSequenceKey = dbSequenceKey;
        this.mapper = mapper;
    }

    @Override
    public String getSequenceId() {
        return DbSequenceGenerator.class.getSimpleName();
    }

    @Override
    protected Optional<SequenceRange> fetchSequence() {
        for (int i = 0; i < 1000000; ++i) {
            SequencePO sequencePO = mapper.selectByPrimaryKey(dbSequenceKey);
            long oldValue = sequencePO.getValue();
            long newValue = oldValue + sequencePO.getStep();
            if (mapper.casUpdateValue(dbSequenceKey, oldValue, newValue) < 1) {
                continue;
            }
            // 溢出
            if (newValue < oldValue) {
                return Optional.empty();
            }
            return Optional.of(new SequenceRange(oldValue + 1, newValue));
        }
        return Optional.empty();
    }
}
