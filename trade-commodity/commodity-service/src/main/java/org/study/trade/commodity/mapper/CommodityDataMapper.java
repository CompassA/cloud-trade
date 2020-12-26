package org.study.trade.commodity.mapper;

import org.apache.ibatis.annotations.Param;
import org.study.trade.commodity.data.CommodityData;

import java.util.List;

public interface CommodityDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSnapshot(CommodityData record);

    int insertSelective(CommodityData record);

    CommodityData selectByPrimaryKey(Integer id);

    CommodityData selectByIdAndVersion(@Param("id") Integer id, @Param("version") Long version);

    List<CommodityData> batchByPrimaryKey(@Param("idList") List<Integer> idList);

    int updateByPrimaryKeySelective(CommodityData record);

    int updateByPrimaryKey(CommodityData record);
}