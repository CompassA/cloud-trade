package org.study.trade.stock.config;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Tomato
 * Created on 2021.08.15
 */
@Configuration
public class RocketMqConfig {

    private final MqConfigProperties mqConfigProperties;

    public RocketMqConfig(MqConfigProperties mqConfigProperties) {
        this.mqConfigProperties = mqConfigProperties;
    }

    @Bean
    public DefaultMQPushConsumer testConsumer() throws MQClientException {
        DefaultMQPushConsumer consumer =
                new DefaultMQPushConsumer(mqConfigProperties.getConsumerGroupName());
        consumer.subscribe(mqConfigProperties.getTopicName(), "*");
        consumer.setNamesrvAddr(mqConfigProperties.getNameServerAddress());
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        // 设置集群消费，通过消费者组的节点不会消费同一条数据
        consumer.setMessageModel(MessageModel.CLUSTERING);

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(
                    List<MessageExt> messages,
                    ConsumeConcurrentlyContext context) {
                for (MessageExt msg : messages) {
                    String msgBody = new String(msg.getBody(), StandardCharsets.UTF_8);
                    String info = "msgBody info: " + msg + "\n" +
                            "msgBody body:" + msgBody + "\n";
                    System.out.println(info);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        return consumer;
    }
}
