package org.study.trade.logger.center.config;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tomato
 * Created on 2021.08.16
 */
@Configuration
public class MqConfig {

    @Autowired
    private RocketMqProperties rocketMqProperties;

    @Bean
    public DefaultMQProducer testProducer() throws MQClientException {
        DefaultMQProducer testProducer =
                new DefaultMQProducer(rocketMqProperties.getProducerGroupName());
        testProducer.setNamesrvAddr(rocketMqProperties.getNameServerAddress());
        testProducer.start();
        return testProducer;
    }
}
