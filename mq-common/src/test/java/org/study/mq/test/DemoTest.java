package org.study.mq.test;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Assert;
import org.junit.Test;
import org.study.mq.MQFactory;

import java.util.List;

/**
 * @author Tomato
 * Created on 2021.01.08
 */
public class DemoTest {

    @Test
    public void demoTest() throws InterruptedException {
        String groupName = "test_demo";
        String nameSrv = "192.168.3.17:9876";
        String topic = "TestDemo";
        DefaultMQProducer defaultMQProducer = null;
        try {
            defaultMQProducer = MQFactory.createDefaultMQProducer(groupName, nameSrv);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
            return;
        }

        DefaultMQPushConsumer consumer = null;
        try {
            MessageListenerConcurrently listener = new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(
                        List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    System.out.printf(Thread.currentThread().getName()
                            + " Receive New Messages: " + new String(list.get(0).getBody()) + "%n");
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            };
            consumer = MQFactory.createConsumer(groupName, topic, "TagA || TagC || TagD", listener);
        } catch (MQClientException e) {
            e.printStackTrace();
            Assert.fail();
        }


        Message message = new Message(topic, "TagA", "HelloWorld".getBytes());
        try {
            SendResult result = defaultMQProducer.send(message);
            Assert.assertEquals(result.getSendStatus(), SendStatus.SEND_OK);
        } catch (MQClientException | RemotingException |MQBrokerException | InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
        }

        Thread.sleep(10000);
        consumer.shutdown();
        defaultMQProducer.shutdown();
    }

    @Test
    public void transactionMQTest() throws InterruptedException {
        String groupName = "test_demo";
        String nameSrv = "192.168.3.17:9876";
        String topic = "TestDemo";
        TransactionMQProducer transactionMQProducer = null;
        try {
            TransactionListener listener = new TestListener();
            transactionMQProducer = MQFactory.createTransactionMQProducer(groupName, nameSrv, listener);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        Message message = new Message(topic, "TagA", "HelloWorld".getBytes());
        try {
            transactionMQProducer.sendMessageInTransaction(message, new Object());
        } catch (MQClientException e) {
            e.printStackTrace();
            Assert.fail();
        }


        DefaultMQPushConsumer consumer = null;
        try {
            MessageListenerConcurrently listener = new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(
                        List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    System.out.printf(Thread.currentThread().getName()
                            + " Receive New Messages: " + new String(list.get(0).getBody()) + "%n");
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            };
            consumer = MQFactory.createConsumer(groupName, topic, "TagA || TagC || TagD", listener);
        } catch (MQClientException e) {
            e.printStackTrace();
            Assert.fail();
        }

        Thread.sleep(10000);
        consumer.shutdown();
        transactionMQProducer.shutdown();
    }


    private static class TestListener implements TransactionListener {

        @Override
        public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            // 模拟事务执行
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }

            // 参数为空，模拟失败的情况
            if (arg == null) {
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
            // 有参数，模拟成功的情况
            return LocalTransactionState.COMMIT_MESSAGE;
        }

        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {
            return LocalTransactionState.UNKNOW;
        }
    }
}
