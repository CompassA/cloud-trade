package org.study.trade.commodity.service.impl;

import lombok.SneakyThrows;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.study.trade.commodity.mapper.data.CommodityData;
import org.study.trade.commodity.test.MockUtil;
import org.study.trade.common.sequence.SequenceException;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

/**
 * @author Tomato
 * Created on 2021.09.08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles(value="test")
public class CommodityServiceTest {

    @Autowired
    private CommodityServiceImpl commodityService;

    @Autowired
    private DataSource dataSource;

    private CommodityData mockData;

    @Before
    public void init() throws SQLException {
        mockData = new CommodityData();
        mockData.setId(1_0006L);
        mockData.setShopId(6L);
        mockData.setVersion(1L);
        mockData.setName("mock name");
        mockData.setPrice(BigDecimal.valueOf(20.0d));

        MockUtil.mockDataBase((ShardingDataSource) dataSource);
    }

    @Test
    @Transactional
    public void insertTwiceTest() throws Exception {
        commodityService.insertCommodity(mockData);
        commodityService.insertCommodity(mockData);
        Assert.assertTrue(true);
    }

    @Test
    public void sequenceTest() throws SequenceException {
        // 测试id后四位是否为shopId后四位相同
        Long sequence = commodityService.generateCommodityId(mockData.getShopId());
        Assert.assertEquals(sequence % 10000, mockData.getShopId() % 10000);
    }

    @Test
    public void casUpdateTest() throws Exception {
        commodityService.insertCommodity(mockData);
        // 启动一百个线程同时更新相同版本的数据，只有一个线程更新成功
        int threadNum = 100;
        CountDownLatch mainWait = new CountDownLatch(threadNum);
        CountDownLatch threadWait = new CountDownLatch(1);
        int[] successCnt = new int[1];
        for (int i = 0; i < threadNum; ++i) {
            new Thread(new Runnable() {
                @Override
                @SneakyThrows
                public void run() {
                    threadWait.await();
                    boolean success = false;
                    try {
                        commodityService.updateCommodity(mockData);
                        success = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (success) {
                        ++successCnt[0];
                    }
                    mainWait.countDown();
                }
            }).start();
        }
        threadWait.countDown();
        mainWait.await();

        Assert.assertEquals(1, successCnt[0]);
    }
}
