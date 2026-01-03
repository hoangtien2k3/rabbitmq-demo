package com.hoangtien2k3.rabbitmq_demo.config;

import lombok.Data;

@Data
public class RabbitQueueProperties {
    private String exchange;
    private String queue;
    private String routingKey;
    private String exchangeType; // e.g., direct, topic, fanout
}
