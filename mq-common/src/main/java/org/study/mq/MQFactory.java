package org.study.mq;

import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;

/**
 * @author Tomato
 * Created on 2021.01.08
 */
public final class MQFactory {

    private MQFactory() {
    }

    public static DefaultMQProducer createDefaultMQProducer(String groupName, String nameSrvAddress)
            throws MQClientException, Exception {
        if (StringUtils.isBlank(nameSrvAddress)) {
            throw new Exception();
        }
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(groupName);
        defaultMQProducer.setNamesrvAddr(nameSrvAddress);
        defaultMQProducer.start();
        return defaultMQProducer;
    }

    public static TransactionMQProducer createTransactionMQProducer(
            String groupName, String nameSrvAddress, TransactionListener listener) throws Exception {
        if (StringUtils.isBlank(nameSrvAddress) || listener == null) {
            throw new Exception();
        }
        TransactionMQProducer transactionMQProducer = new TransactionMQProducer(groupName);
        transactionMQProducer.setTransactionListener(listener);
        transactionMQProducer.setNamesrvAddr(nameSrvAddress);
        transactionMQProducer.start();
        return transactionMQProducer;
    }

    public static DefaultMQPushConsumer createConsumer(
            final String groupName, String topic, String tag, MessageListenerConcurrently messageListener)
            throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.subscribe(topic, tag);
        consumer.registerMessageListener(messageListener);
        consumer.start();
        return consumer;
    }
}
