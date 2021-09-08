package org.study.trade.commodity.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.trade.commodity.mapper.CommodityDataMapper;
import org.study.trade.commodity.mapper.data.CommodityData;
import org.study.trade.common.sequence.SequenceException;
import org.study.trade.common.sequence.SequenceGenerator;

/**
 * @author Tomato
 * Created on 2020.12.26
 */
@Service
public class CommodityServiceImpl {

    @Autowired
    private CommodityDataMapper commodityMapper;

    @Autowired
    private SequenceGenerator sequenceGenerator;

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
    public void insertCommodity(CommodityData commodityData) throws Exception {
        if (StringUtils.isBlank(commodityData.getName())
                || commodityData.getId() == null
                || commodityData.getShopId() == null
                || commodityData.getPrice() == null
                || commodityData.getPrice().doubleValue() < 0) {
            throw new Exception("invalid input");
        }
        commodityData.setVersion(0L);
        try {
            if (commodityMapper.insertSelective(commodityData) < 1) {
                throw new Exception("insert failed");
            }
        } catch (DuplicateKeyException exception) {
            // 若重复插入，正常返回，保持幂等性
            return;
        }
        if (commodityMapper.insertSnapshot(commodityData) < 1) {
            throw new Exception("insert snapshot failed");
        }
    }

    public Long generateCommodityId(Long shopId) throws SequenceException {
        long id = sequenceGenerator.nextValue();
        return (id % 100000000000000L * 10000) + shopId % 10000;
    }
}
