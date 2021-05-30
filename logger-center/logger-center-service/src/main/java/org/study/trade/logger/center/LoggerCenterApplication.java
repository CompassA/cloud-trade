package org.study.trade.logger.center;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Tomato
 * Created on 2021.05.30
 */
@EnableDubbo
@SpringBootApplication
public class LoggerCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoggerCenterApplication.class);
    }
}
