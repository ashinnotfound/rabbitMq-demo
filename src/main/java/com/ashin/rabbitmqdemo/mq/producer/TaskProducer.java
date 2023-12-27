package com.ashin.rabbitmqdemo.mq.producer;

import com.ashin.rabbitmqdemo.mq.factory.MqConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
public class TaskProducer {

    @Resource
    private MqConnectionFactory factory;

    @PostConstruct
    public void init() {

        String TASK_QUEUE_NAME = "TASK_QUEUE";

        try (Connection connection = factory.getFactory().newConnection();
             Channel channel = connection.createChannel()) {
            // 第二个bool为true 意为支持消息可持久 即服务宕机了消息不会丢失
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

            String message = "task test";

            // MessageProperties.PERSISTENT_TEXT_PLAIN 意为该条消息为持久的
            channel.basicPublish("", TASK_QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
