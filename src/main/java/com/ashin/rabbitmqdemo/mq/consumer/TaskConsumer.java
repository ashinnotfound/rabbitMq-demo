package com.ashin.rabbitmqdemo.mq.consumer;

import com.ashin.rabbitmqdemo.mq.factory.MqConnectionFactory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
public class TaskConsumer {

    @Resource
    private MqConnectionFactory factory;

    @PostConstruct
    public void init() {

        String TASK_QUEUE_NAME = "TASK_QUEUE";

        try (Connection connection = factory.getFactory().newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            System.out.println(" [*] Waiting for messages.");

            // 公平调度
            // 当该消费者没有回复时 生产者不发送新消息给该消费者
            channel.basicQos(1);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

                System.out.println(" [x] Received '" + message + "'");

                // 做些什么

                System.out.println(" [x] Done");
                // 回复生产者
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };

            //不自动回复
            boolean autoAck = false;
            channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
            });
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
