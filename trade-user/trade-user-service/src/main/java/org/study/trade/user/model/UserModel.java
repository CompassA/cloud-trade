package org.study.trade.user.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Tomato
 * Created on 2021.09.11
 */
@Getter
@Setter
public class UserModel {

    private Long id;

    private String nick;

    private Byte gender;

    private Long telephone;

    private String password;

    private Date createTime;

    private Date updateTime;

}
