package org.study.trade.commodity.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommodityData {
    private Integer id;

    private Integer shopId;

    private String name;

    private BigDecimal price;

    private Long version;

    private Date createTime;

    private Date updateTime;
}