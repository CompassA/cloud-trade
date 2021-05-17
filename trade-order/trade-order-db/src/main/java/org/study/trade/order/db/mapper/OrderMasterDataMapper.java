package org.study.trade.order.db.mapper;

import org.study.trade.order.db.mapper.data.OrderMasterData;

public interface OrderMasterDataMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(OrderMasterData record);

    int insertSelective(OrderMasterData record);

    OrderMasterData selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(OrderMasterData record);

    int updateByPrimaryKey(OrderMasterData record);
}