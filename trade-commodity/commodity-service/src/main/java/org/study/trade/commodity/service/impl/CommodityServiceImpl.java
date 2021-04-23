package org.study.trade.commodity.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.trade.commodity.mapper.data.CommodityData;
import org.study.trade.commodity.mapper.CommodityDataMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Tomato
 * Created on 2020.12.26
 */
@Service
public class CommodityServiceImpl {

    @Autowired
    private CommodityDataMapper commodityMapper;

    public List<CommodityData> batchSelectByKey(final List<Long> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }
        return commodityMapper.batchByPrimaryKey(keys);
    }

    public Optional<CommodityData> selectByKeyAndVersion(Long id, Long version) {
        if (id == null || version == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(commodityMapper.selectByIdAndVersion(id, version));
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCommodity(CommodityData commodityData) throws Exception {
        if (commodityData.getId() == null || commodityData.getVersion() == null) {
            throw new Exception("version id is null");
        }
        if (commodityMapper.updateByPrimaryKeySelective(commodityData) < 1) {
            throw new Exception("update commodity failed");
        }
        commodityData.setVersion(commodityData.getVersion() + 1);
        if (commodityMapper.insertSnapshot(commodityData) < 1) {
            throw new Exception("insert snapshot failed");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public CommodityData insertCommodity(CommodityData commodityData) throws Exception {
        if (StringUtils.isBlank(commodityData.getName()) || commodityData.getShopId() == null ||
                commodityData.getPrice() == null || commodityData.getPrice().doubleValue() < 0) {
            throw new Exception("invalid input");
        }
        commodityData.setVersion(0L);
        if (commodityMapper.insertSelective(commodityData) < 1) {
            throw new Exception("insert failed");
        }
        if (commodityMapper.insertSnapshot(commodityData) < 1) {
            throw new Exception("insert snapshot failed");
        }
        return commodityData;
    }
}
