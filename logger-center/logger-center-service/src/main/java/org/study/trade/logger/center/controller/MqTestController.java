package org.study.trade.logger.center.controller;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @author Tomato
 * Created on 2021.08.16
 */
@RestController
public class MqTestController {

    @Autowired
    private MQProducer producer;

    @PostMapping("/test/mq/sync")
    public String echoSync(@RequestBody String body)
            throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        producer.send(
                new Message(
                        "test-mq-dev",
                        "",
                        body.getBytes(StandardCharsets.UTF_8)
                )
        );
        return body;
    }
}
