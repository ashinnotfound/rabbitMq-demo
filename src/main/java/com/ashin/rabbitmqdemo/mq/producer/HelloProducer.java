package com.ashin.rabbitmqdemo.mq.producer;

import com.ashin.rabbitmqdemo.mq.factory.MqConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class HelloProducer {

    @Resource
    private MqConnectionFactory factory;

    @PostConstruct
    public void init() {

        String QUEUE_NAME = "HELLO";

        try (Connection connection = factory.getFactory().newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
