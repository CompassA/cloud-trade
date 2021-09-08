package org.study.trade.commodity.test;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Tomato
 * Created on 2021.09.09
 */
public class MockUtil {

    public static void mockDataBase(ShardingDataSource shardingDataSource) throws SQLException {
        DataSource sequence = shardingDataSource.getDataSourceMap().get("sequence");
        Connection sequenceConnection = sequence.getConnection();
        PreparedStatement createStatement = sequenceConnection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS sequence (" +
                        "        id INT NOT NULL," +
                        "        name VARCHAR(64) NOT NULL," +
                        "        value BIGINT NOT NULL DEFAULT 0," +
                        "        step BIGINT NOT NULL DEFAULT 100," +
                        "        create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                        "        update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                        "        PRIMARY KEY(id)" +
                        "    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
        );
        createStatement.execute();
        PreparedStatement insertStatement = sequenceConnection.prepareStatement(
                "INSERT INTO sequence(id, name, step, value) VALUES (1, 'commodity_sequence', 2000, 0) " +
                        "ON DUPLICATE KEY UPDATE value=0;"
        );
        insertStatement.execute();

        for (int i = 0; i < 2; ++i) {
            DataSource dataSource = shardingDataSource.getDataSourceMap()
                    .get(String.format("product-db-%d", i));
            Connection connection = dataSource.getConnection();
            for (int j = 0; j < 8; ++j) {
                String commodityTableName = "commodity_" + j;
                String commoditySnapShotTableName = "commodity_snapshot_" + j;
                PreparedStatement createCommodityTable = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS " + commodityTableName + "(" +
                                "id BIGINT NOT NULL," +
                                "shop_id BIGINT NOT NULL," +
                                "name VARCHAR(128) NOT NULL," +
                                "price DECIMAL(14,2) NOT NULL," +
                                "version BIGINT NOT NULL," +
                                "create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                                "update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                                "PRIMARY KEY(id)," +
                                "INDEX idx_shop_id_" + j + "(shop_id)" +
                                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
                );

                PreparedStatement createSnapshotTable = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS " + commoditySnapShotTableName + "(\n" +
                                "                id BIGINT NOT NULL,\n" +
                                "                version BIGINT NOT NULL,\n" +
                                "                shop_id BIGINT NOT NULL,\n" +
                                "                name VARCHAR(128) NOT NULL,\n" +
                                "                price DECIMAL(14,2) NOT NULL,\n" +
                                "                create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                                "                update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                                "                PRIMARY KEY(id, version)\n" +
                                "            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
                createCommodityTable.execute();
                createSnapshotTable.execute();
            }
        }
    }
}
