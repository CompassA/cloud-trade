package org.study.trade.logger.center.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Tomato
 * Created on 2021.08.16
 */
@Getter
@Setter
@Component
@PropertySource("classpath:mq-config.properties")
@ConfigurationProperties(prefix = "mq-config")
public class RocketMqProperties {

    private String producerGroupName;

    private String nameServerAddress;
}
