package com.hoangtien2k3.rabbitmq_demo.publisher;

import java.io.Serializable;

public interface RabbitPublisher<T extends Serializable> {
    void publish(String exchange, String routingKey, T data);

    void publish(String queueName, T data);
}
