package org.study.trade.balance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Tomato
 * Created on 2020.12.29
 */
@SpringBootApplication
@MapperScan("org.study.trade.balance.mapper")
public class TradeBalanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeBalanceApplication.class, args);
    }

}
