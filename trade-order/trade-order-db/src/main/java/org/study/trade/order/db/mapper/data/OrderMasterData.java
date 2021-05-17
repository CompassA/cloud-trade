package org.study.trade.order.db.mapper.data;

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
public class OrderMasterData {
    private Long orderId;

    private Long userId;

    private Long shopId;

    private Long telephone;

    private BigDecimal amount;

    private Byte orderStatus;

    private Date createTime;

    private Date updateTime;
}