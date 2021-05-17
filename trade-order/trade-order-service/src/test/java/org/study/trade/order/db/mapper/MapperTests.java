package org.study.trade.order.db.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.study.trade.order.db.mapper.data.OrderMasterData;

import java.math.BigDecimal;

/**
 * @author Tomato
 * Created on 2021.05.15
 */
@SpringBootTest
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class MapperTests {

    @Autowired
    private OrderMasterDataMapper orderMasterMapper;

    @Test
    public void shardingInsertTest() {
        for (long i = 0; i < 512; ++i) {
            Assert.assertEquals(orderMasterMapper.insertSelective(mockOrder(i, i)), 1);
        }
    }

    private OrderMasterData mockOrder(final Long orderId, final Long userId) {
        return new OrderMasterData()
                .setOrderId(orderId)
                .setUserId(userId)
                .setShopId(1L)
                .setTelephone(19545434565L)
                .setAmount(BigDecimal.valueOf(12.5));
    }
}
