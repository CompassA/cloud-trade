package org.study.trade.balance.mapper;

import org.apache.ibatis.annotations.Param;
import org.study.trade.balance.data.BalanceData;

import java.math.BigDecimal;

public interface BalanceDataMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(BalanceData record);

    int insertSelective(BalanceData record);

    BalanceData selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(BalanceData record);

    int updateByPrimaryKey(BalanceData record);

    /**
     * 金额扣减
     * @param userId 用户id
     * @param newLogId 新的交易日志号
     * @param curLogId 当前的交易日志号
     * @param amount 扣减金额
     * @return 影响的行数
     */
    int reduce(@Param("userId") int userId, @Param("newLogId") int newLogId,
               @Param("curLogId") int curLogId, @Param("amount") BigDecimal amount);
}