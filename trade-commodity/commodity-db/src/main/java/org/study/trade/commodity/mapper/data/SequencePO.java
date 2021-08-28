package org.study.trade.commodity.mapper.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SequencePO {
    private Integer id;

    private String name;

    private Long value;

    private Long step;

    private Timestamp createTime;

    private Timestamp updateTime;
}