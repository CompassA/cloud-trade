package org.study.trade.commodity.mapper;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.study.trade.commodity.mapper.data.CommodityData;
import org.study.trade.commodity.service.impl.CommodityServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author Tomato
 * Created on 2020.12.26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MapperTest {

    @Autowired
    private CommodityDataMapper commodityPOMapper;

    @Autowired
    private CommodityServiceImpl commodityService;

    @Test
    public void insertTest() {
        CommodityData mock = new CommodityData();
        mock.setName("测试商品");
        mock.setShopId(1L);
        mock.setPrice(new BigDecimal(20));
        try {
            CommodityData insertData = commodityService.insertCommodity(mock);
            Optional<CommodityData> res = commodityService.selectByKeyAndVersion(
                    insertData.getId(), insertData.getVersion());
            Assert.assertTrue(res.isPresent());
            CommodityData snapshot = res.get();
            System.out.println("insert data: " + insertData);
            System.out.println("snapshot: " + snapshot);
            Assert.assertEquals(snapshot.getId(), insertData.getId());
            Assert.assertEquals(snapshot.getVersion(), insertData.getVersion());
        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }
    }

    @Test
    public void updateTest() {
        List<CommodityData> resList = commodityService.batchSelectByKey(Lists.newArrayList(1L));
        Assert.assertEquals(resList.size(), 1);
        CommodityData res = resList.get(0);
        Long oldVersion = res.getVersion();
        res.setPrice(BigDecimal.valueOf(Math.round(Math.abs(Math.random()) % 1000), 2));
        try {
            commodityService.updateCommodity(res);
            Optional<CommodityData> snapshot =
                    commodityService.selectByKeyAndVersion(res.getId(), oldVersion + 1);
            Assert.assertTrue(snapshot.isPresent());
            Assert.assertEquals(res.getPrice().compareTo(snapshot.get().getPrice()), 0);
            Assert.assertEquals(oldVersion + 1, snapshot.get().getVersion().longValue());
        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }

        res.setVersion(oldVersion);
        boolean throwException = false;
        try {
            commodityService.updateCommodity(res);
        } catch (Exception e) {
            throwException = true;
            e.printStackTrace();
        }
        Assert.assertTrue(throwException);
    }
}
