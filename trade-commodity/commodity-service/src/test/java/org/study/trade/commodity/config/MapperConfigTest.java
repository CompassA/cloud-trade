package org.study.trade.commodity.config;

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
import org.study.trade.commodity.mapper.CommodityDataMapper;
import org.study.trade.commodity.mapper.data.CommodityData;
import org.study.trade.commodity.test.MockUtil;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Random;

/**
 * @author Tomato
 * Created on 2020.12.26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles(value="test")
public class MapperConfigTest {

    @Autowired
    private CommodityDataMapper commodityPOMapper;

    @Autowired
    private DataSource dataSource;

    private Map<String, DataSource> shardingDataSourceMap;

    private CommodityData mockData;

    @PostConstruct
    public void postConstruct() {
        this.shardingDataSourceMap = ((ShardingDataSource) dataSource).getDataSourceMap();
    }

    @Before
    public void init() throws SQLException {
        mockData = new CommodityData();
        mockData.setId(1_0001L);
        mockData.setShopId(1L);
        mockData.setVersion(1L);
        mockData.setName("mock name");
        mockData.setPrice(BigDecimal.valueOf(20.0d));

        MockUtil.mockDataBase((ShardingDataSource) dataSource);
    }

    /** 测试路由插入的正确性 */
    @Test
    public void insertTest() throws SQLException {
        Random random = new Random();
        for (int i = 0; i < 8; ++i) {
            // mock 商品id 和店铺id
            long mockShopId = Math.abs(random.nextLong());
            long mockId = Math.abs(random.nextLong()) / 10000 * 10000 + mockShopId % 10000;
            mockData.setId(mockId);
            mockData.setShopId(mockShopId);
            mockData.setVersion(Math.abs(random.nextLong()));

            // 商品表插入一条数据
            commodityPOMapper.insertSelective(mockData);
            // 商品快照表插入一条数据
            commodityPOMapper.insertSnapshot(mockData);

            // 测试数据源一致
            String dataSourceName = "product-db-" + mockShopId % 2;
            String snapShotDataSourceName = "product-db-" + mockId % 2;
            Assert.assertEquals(dataSourceName, snapShotDataSourceName);

            // 用真实sql查询数据是否存在
            DataSource dbSource = shardingDataSourceMap.get(dataSourceName);
            try (Connection connection = dbSource.getConnection()) {
                String checkSQL = String.format("SELECT * FROM commodity_%d WHERE id = %d",
                        mockId % 8,
                        mockId);
                ResultSet resultSet = connection.prepareStatement(checkSQL).executeQuery();
                resultJudge(resultSet, mockData);

                checkSQL = String.format("SELECT * FROM commodity_snapshot_%d WHERE id = %d",
                        mockData.getVersion() % 8,
                        mockId);
                resultSet = connection.prepareStatement(checkSQL).executeQuery();
                resultJudge(resultSet, mockData);
            }

            Assert.assertEquals(1, commodityPOMapper.deleteByKeyAndShopId(mockId, mockShopId));
            Assert.assertEquals(1, commodityPOMapper.deleteSnapshotByPrimaryKey(mockId, mockData.getVersion()));
        }
    }

    private void resultJudge(ResultSet resultSet, CommodityData data) throws SQLException {
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals((long) data.getId(), resultSet.getLong("id"));
        Assert.assertEquals((long) data.getShopId(), resultSet.getLong("shop_id"));
        Assert.assertEquals(((long) data.getVersion()), resultSet.getLong("version"));
        Assert.assertEquals(data.getName(), resultSet.getString("name"));
        Assert.assertEquals(0, data.getPrice().compareTo(resultSet.getBigDecimal("price")));
    }
}
