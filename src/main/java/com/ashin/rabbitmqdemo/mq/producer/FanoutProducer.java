package com.ashin.rabbitmqdemo.mq.producer;

import com.ashin.rabbitmqdemo.mq.factory.MqConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
public class FanoutProducer {
    @Resource
    private MqConnectionFactory factory;

    @PostConstruct
    public void init() {

        String EXCHANGE_NAME = "FANOUT_EXCHANGE";

        try (Connection connection = factory.getFactory().newConnection(); Channel channel = connection.createChannel()) {

            // direct, topic, headers, fanout
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            String message = "fanout test";

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
