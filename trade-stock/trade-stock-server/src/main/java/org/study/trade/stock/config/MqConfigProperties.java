package org.study.trade.stock.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Tomato
 * Created on 2021.08.15
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@PropertySource(value = "classpath:rocket-mq-config.properties")
@ConfigurationProperties(prefix = "mq-config")
public class MqConfigProperties {

    private String topicName;

    private String consumerGroupName;

    private String nameServerAddress;
}
