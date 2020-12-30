package org.study.trade.balance;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.study.trade.balance.data.BalanceData;
import org.study.trade.balance.mapper.BalanceDataMapper;
import org.study.trade.balance.service.BalanceServiceImpl;
import org.study.trade.balance.service.model.TradeModel;

import java.math.BigDecimal;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tomato
 * Created on 2020.12.30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TradeTest {

    @Autowired
    private BalanceServiceImpl balanceService;

    @Autowired
    private BalanceDataMapper balanceDataMapper;

    /**
     * 扣款测试
     */
    @Test
    public void balanceTest() {
        // 设置账户为1000
        BalanceData fromAccountBalance = resetTestAccountBalance();

        // 扣除100
        TradeModel tradeModel = TradeModel.builder()
                .fromUserId(1)
                .toUserId(2)
                .amount(new BigDecimal(100))
                .build();
        try {
            BigDecimal leftBalance = balanceService.reduce(tradeModel);
            int res = leftBalance.compareTo(fromAccountBalance.getBalance().subtract(tradeModel.getAmount()));
            Assert.assertEquals(0, res);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    /**
     * 余额不足测试
     */
    @Test
    public void exceptionTest() {
        BalanceData fromAccountBalance = resetTestAccountBalance();
        TradeModel tradeModel = TradeModel.builder()
                .fromUserId(1)
                .toUserId(2)
                .amount(fromAccountBalance.getBalance().add(BigDecimal.valueOf(0.01)))
                .build();
        try {
            balanceService.reduce(tradeModel);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "not enough");
            return;
        }
        Assert.fail();
    }

    /**
     * cas测试
     */
    @Test
    public void threadTest() {
        resetTestAccountBalance();
        CyclicBarrier selectBarrier = new CyclicBarrier(2);
        AtomicInteger cnt = new AtomicInteger(0);
        Runnable r = () -> {
            int userId = 1;
            BalanceData balanceData = balanceDataMapper.selectByPrimaryKey(userId);
            try {
                selectBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
                return;
            }
            cnt.getAndAdd(
                    balanceDataMapper.reduce(
                        userId, balanceData.getLogId() + 1, balanceData.getLogId(), new BigDecimal(1)));

        };
        Thread t1 = new Thread(r);
        t1.start();
        Thread t2 = new Thread(r);
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
            return;
        }
        Assert.assertEquals(1, cnt.get());
    }

    private BalanceData resetTestAccountBalance() {
        BalanceData fromAccountBalance = new BalanceData()
                .setBalance(new BigDecimal(1000))
                .setUserId(1);
        balanceDataMapper.updateByPrimaryKeySelective(fromAccountBalance);
        return fromAccountBalance;
    }

}
