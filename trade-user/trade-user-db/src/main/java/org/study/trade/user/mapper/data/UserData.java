package org.study.trade.user.mapper.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
public class UserData {
    private Long id;

    private String nick;

    private Byte gender;

    private Integer telephone;

    private String password;

    private Date createTime;

    private Date updateTime;
}