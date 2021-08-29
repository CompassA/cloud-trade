package org.study.trade.commodity.sequence;

import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.study.trade.common.sequence.SequenceException;
import org.study.trade.common.sequence.SequenceGenerator;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author Tomato
 * Created on 2021.08.28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles(value="test")
public class DbSequenceTest {

    @Autowired
    @Qualifier("dbSequenceGenerator")
    private SequenceGenerator sequenceGenerator;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void init() throws SQLException {
        DataSource sequence = ((ShardingDataSource) dataSource).getDataSourceMap().get("sequence");
        Connection connection = sequence.getConnection();
        PreparedStatement createStatement = connection.prepareStatement(
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
        PreparedStatement insertStatement = connection.prepareStatement(
                "INSERT INTO sequence(id, name, step, value) VALUES (1, 'commodity_sequence', 2000, 0);"
        );
        insertStatement.execute();
    }

    /**
     * 并发生成号段测试
     */
    @Test
    public void concurrentTest() throws InterruptedException {
        // 开启threadNum个线程
        // 每个线程获取targetIdNum个id
        // 测试是否生成了 threadNum * targetIdNum 个id
        Map<Long, Object> usedId = new ConcurrentHashMap<>();
        int threadNum = 100;
        int targetIdNum = 100000;

        CountDownLatch threadStart = new CountDownLatch(1);
        CountDownLatch mainAwait = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; ++i) {
            new Thread(() -> {
                try {
                    threadStart.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (int j = 0; j < targetIdNum; ++j) {
                    try {
                        long id = sequenceGenerator.nextValue();
                        usedId.put(id, DbSequenceTest.class);
                    } catch (SequenceException e) {
                        e.printStackTrace();
                    }
                }

                mainAwait.countDown();
            }).start();
        }
        threadStart.countDown();
        System.out.println("start generate sequence");
        mainAwait.await();

        // 检测数量
        System.out.println("end");
        Assert.assertEquals(usedId.size(), threadNum * targetIdNum);

        // 检测是否连续
        Iterator<Long> it = new TreeSet<>(usedId.keySet()).iterator();
        Long pre = it.next();
        while (it.hasNext()) {
            Long cur = it.next();
            if (cur != pre + 1) {
                System.out.printf("fail: pre = %d, cur = %d\n", pre, cur);
                Assert.fail();
            }
            pre = cur;
        }
    }
}
