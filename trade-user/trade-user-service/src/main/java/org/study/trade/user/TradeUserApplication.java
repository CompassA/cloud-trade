package org.study.trade.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Tomato
 * Created on 2021.04.25
 */
@SpringBootApplication
@MapperScan(basePackages = {"org.study.trade.user.mapper"})
public class TradeUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeUserApplication.class, args);
    }
}
