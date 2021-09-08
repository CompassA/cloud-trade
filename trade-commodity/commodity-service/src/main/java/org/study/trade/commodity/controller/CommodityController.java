package org.study.trade.commodity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.study.trade.commodity.mapper.CommodityDataMapper;

/**
 * @author Tomato
 * Created on 2020.12.26
 */
@RestController
public class CommodityController {

    @Autowired
    private CommodityDataMapper commodityMapper;
}
