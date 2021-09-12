package org.study.trade.user.mapper.data;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class SellerPO {
    private Long id;

    private String name;

    private BigDecimal remarkScore;

    private Byte disabledFlag;

    private Date createTime;

    private Date updateTime;
}