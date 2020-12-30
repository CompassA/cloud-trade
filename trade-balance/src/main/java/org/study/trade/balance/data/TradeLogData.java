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
public class TradeLogData {
    private Integer logId;

    private BigDecimal amount;

    private Integer fromAccount;

    private Integer toAccount;

    private Date updateTime;
}