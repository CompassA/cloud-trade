package org.study.trade.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Tomato
 * Created on 2021.04.25
 */
@EnableDubbo
@SpringBootApplication
public class TradeUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeUserApplication.class, args);
    }
}
