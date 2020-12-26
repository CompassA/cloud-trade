package org.study.trade.commodity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Tomato
 * Created on 2020.12.25
 */
@MapperScan(
        basePackages = {
                "org.study.trade.commodity.mapper"
        }
)
@SpringBootApplication(
        scanBasePackages = {
                "org.study.trade.commodity"
        }
)
public class CommodityApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommodityApplication.class, args);
    }

}
