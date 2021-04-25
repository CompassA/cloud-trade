package org.study.trade.commodity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.study.trade.commodity.mapper.CommodityDataMapper;
import org.study.trade.commodity.mapper.data.CommodityData;

/**
 * @author Tomato
 * Created on 2020.12.26
 */
@RestController
public class CommodityController {

    @Autowired
    private CommodityDataMapper commodityMapper;

    @GetMapping("/trade/api/commodity")
    public CommodityData getCommodityById(@RequestParam("id") Long id) {
        return commodityMapper.selectByPrimaryKey(id);
    }


}
