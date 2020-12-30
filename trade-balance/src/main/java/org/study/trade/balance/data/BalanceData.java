package org.study.trade.balance.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class BalanceData {
    private Integer userId;

    private BigDecimal balance;

    private Integer logId;

    private Date createTime;

    private Date updateTime;
}