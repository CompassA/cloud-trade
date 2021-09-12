package org.study.trade.user.mapper;

import org.study.trade.user.mapper.data.SellerPO;

public interface SellerPOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SellerPO record);

    int insertSelective(SellerPO record);

    SellerPO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SellerPO record);

    int updateByPrimaryKey(SellerPO record);
}