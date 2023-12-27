package com.ashin.rabbitmqdemo.mq.consumer;

import com.ashin.rabbitmqdemo.mq.factory.MqConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
public class DirectConsumer {
    @Resource
    private MqConnectionFactory factory;

    @PostConstruct
    public void init() {

        String EXCHANGE_NAME = "DIRECT_EXCHANGE";
        String BINDING_KEY_1 = "INFO";
//        String BINDING_KEY_2 = "WARNING";

        try (Connection connection = factory.getFactory().newConnection(); Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            String queueName = channel.queueDeclare().getQueue();

            channel.queueBind(queueName, EXCHANGE_NAME, BINDING_KEY_1);

            System.out.println(" [*] Waiting for messages.");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" +
                        delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
