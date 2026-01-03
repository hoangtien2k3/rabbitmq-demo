package com.hoangtien2k3.rabbitmq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitQueueProperties {
    private Map<String, RabbitQueueProperties> queues;
    private String exchange;
    private String queue;
    private String routingKey;
    private String exchangeType;
}
