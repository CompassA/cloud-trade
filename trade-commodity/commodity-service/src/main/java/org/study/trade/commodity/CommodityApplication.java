package org.study.trade.commodity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Tomato
 * Created on 2020.12.25
 */
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
