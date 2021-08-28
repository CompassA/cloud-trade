package org.study.trade.commodity.sequence;

import org.apache.ibatis.annotations.Param;
import org.study.trade.commodity.mapper.data.SequencePO;

public interface SequencePOMapper {

    /**
     * 根据主键查找sequence
     * @param id sequence主键
     * @return sequence数据
     */
    SequencePO selectByPrimaryKey(int id);

    /**
     * cas方式更新sequence
     * @param id sequence主键
     * @param oldValue 内存中的值
     * @param newValue 要更新的值
     * @return 更新影响的行数
     */
    int casUpdateValue(
            @Param("id") int id,
            @Param("oldValue") long oldValue,
            @Param("newValue") long newValue);
}