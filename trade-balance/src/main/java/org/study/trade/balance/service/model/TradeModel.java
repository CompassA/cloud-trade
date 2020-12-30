package org.study.trade.balance.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author Tomato
 * Created on 2020.12.29
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeModel {

    private int fromUserId;

    private int toUserId;

    private BigDecimal amount;
}
