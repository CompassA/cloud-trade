package org.study.trade.commodity.mapper;

import org.apache.ibatis.annotations.Param;
import org.study.trade.commodity.mapper.data.CommodityData;

import java.util.List;

public interface CommodityDataMapper {
    int deleteByKeyAndShopId(@Param("id") Long id, @Param("shopId") Long shopId);

    int deleteSnapshotByPrimaryKey(@Param("id") Long id, @Param("version") Long version);

    int insertSnapshot(CommodityData record);

    int insertSelective(CommodityData record);

    CommodityData selectByIdAndShopId(@Param("id") Long id, @Param("shopId") Long shopId);

    CommodityData selectByIdAndVersion(@Param("id") Long id, @Param("version") Long version);

    List<CommodityData> batchByPrimaryKey(@Param("idList") List<Long> idList);

    int updateByPrimaryKeySelective(CommodityData record);

    int updateByPrimaryKey(CommodityData record);
}