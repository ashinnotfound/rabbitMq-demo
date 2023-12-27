package com.ashin.rabbitmqdemo.mq.factory;

import com.rabbitmq.client.ConnectionFactory;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MqConnectionFactory {

    private ConnectionFactory factory;

    @PostConstruct
    public void init() {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
    }

}
