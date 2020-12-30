package org.study.trade.balance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.trade.balance.data.BalanceData;
import org.study.trade.balance.data.TradeLogData;
import org.study.trade.balance.mapper.BalanceDataMapper;
import org.study.trade.balance.mapper.TradeLogDataMapper;
import org.study.trade.balance.service.model.TradeModel;

import java.math.BigDecimal;

/**
 * @author Tomato
 * Created on 2020.12.29
 */
@Service
public class BalanceServiceImpl {

    @Autowired
    private BalanceDataMapper balanceDataMapper;

    @Autowired
    private TradeLogDataMapper tradeLogDataMapper;

    @Transactional(rollbackFor = Exception.class)
    public BigDecimal reduce(TradeModel tradeModel) throws Exception {
        if (tradeModel == null || tradeModel.getAmount() == null || tradeModel.getAmount().doubleValue() <= 0) {
            return BigDecimal.valueOf(0);
        }
        // 查询余额
        BalanceData userBalance = balanceDataMapper.selectByPrimaryKey(tradeModel.getFromUserId());
        if (userBalance == null || userBalance.getBalance().compareTo(tradeModel.getAmount()) < 0) {
            throw new Exception("not enough");
        }

        // 插入交易流水
        TradeLogData tradeLogData = new TradeLogData()
                .setAmount(tradeModel.getAmount())
                .setFromAccount(tradeModel.getFromUserId())
                .setToAccount(tradeModel.getToUserId());
        if (tradeLogDataMapper.insertSelective(tradeLogData) != 1) {
            return BigDecimal.valueOf(0);
        }

        // 更新余额
        if (balanceDataMapper.reduce(tradeModel.getFromUserId(), tradeLogData.getLogId(),
                userBalance.getLogId(), tradeModel.getAmount()) != 1) {
            throw new Exception("cas error");
        }

        return userBalance.getBalance().subtract(tradeModel.getAmount());
    }
}
