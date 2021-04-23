package org.study.trade.commodity.mapper;

import org.apache.ibatis.annotations.Param;
import org.study.trade.commodity.mapper.data.CommodityData;

import java.util.List;

public interface CommodityDataMapper {
    int deleteByPrimaryKey(Long id);

    int insertSnapshot(CommodityData record);

    int insertSelective(CommodityData record);

    CommodityData selectByPrimaryKey(Long id);

    CommodityData selectByIdAndVersion(@Param("id") Long id, @Param("version") Long version);

    List<CommodityData> batchByPrimaryKey(@Param("idList") List<Long> idList);

    int updateByPrimaryKeySelective(CommodityData record);

    int updateByPrimaryKey(CommodityData record);
}