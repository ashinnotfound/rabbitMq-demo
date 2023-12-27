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
public class DirectProducer {
    @Resource
    private MqConnectionFactory factory;

    @PostConstruct
    public void init() {

        String EXCHANGE_NAME = "DIRECT_EXCHANGE";
        String BINDING_KEY_1 = "INFO";
        String BINDING_KEY_2 = "WARNING";

        try (Connection connection = factory.getFactory().newConnection(); Channel channel = connection.createChannel()) {

            // direct, topic, headers, fanout
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            // topic模式是模糊的direct binding_key由单词.单词构成, 即 com.ashin, *.ashin.*, #.ashin
            // * 对应一个单词
            // # 对应任意单词

            String message = "direct-";

            channel.basicPublish(EXCHANGE_NAME, BINDING_KEY_1, null, (message + BINDING_KEY_1).getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + BINDING_KEY_1 + "':'" + message + "'");
            channel.basicPublish(EXCHANGE_NAME, BINDING_KEY_2, null, (message + BINDING_KEY_2).getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + BINDING_KEY_2 + "':'" + message + "'");
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
