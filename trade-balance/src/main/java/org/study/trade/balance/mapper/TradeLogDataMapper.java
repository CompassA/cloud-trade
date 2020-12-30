package org.study.trade.balance.mapper;

import org.study.trade.balance.data.TradeLogData;

public interface TradeLogDataMapper {
    int deleteByPrimaryKey(Integer logId);

    int insert(TradeLogData record);

    int insertSelective(TradeLogData record);

    TradeLogData selectByPrimaryKey(Integer logId);

    int updateByPrimaryKeySelective(TradeLogData record);

    int updateByPrimaryKey(TradeLogData record);
}