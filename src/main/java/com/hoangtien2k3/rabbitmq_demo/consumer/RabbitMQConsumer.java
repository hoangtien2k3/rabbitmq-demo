package com.hoangtien2k3.rabbitmq_demo.consumer;

import com.hoangtien2k3.rabbitmq_demo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQConsumer {

    @RabbitListener(queues = "${spring.rabbitmq.queues.hello.queue}")
    public void consumeHello(String message) {
        log.info("📥 [Simple] Received from hello.queue: {}", message);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.user.queue}")
    public void consumeUser(User user) {
        log.info("📥 [Direct] Received User creation: {} ({})", user.getUsername(), user.getEmail());
    }
}
