package com.hoangtien2k3.rabbitmq_demo.config;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueInitialization implements InitializingBean {

    private final List<RabbitQueueProperties> rabbitQueuePropertiesList;
    private final ConnectionFactory connectionFactory;

    private static final String X_MAX_PRIORITY = "x-max-priority";

    @Override
    public void afterPropertiesSet() {
        try (Connection connection = connectionFactory.createConnection();
                Channel channel = connection.createChannel(false)) {

            Map<String, Object> args = new HashMap<>();
            args.put(X_MAX_PRIORITY, 100);

            for (RabbitQueueProperties queue : rabbitQueuePropertiesList) {
                try {
                    // Determine exchange type from properties, default to TOPIC
                    BuiltinExchangeType type = BuiltinExchangeType.TOPIC;
                    if (queue.getExchangeType() != null) {
                        try {
                            type = BuiltinExchangeType.valueOf(queue.getExchangeType().toUpperCase());
                        } catch (IllegalArgumentException e) {
                            log.warn("Invalid exchange type '{}' for exchange '{}', defaulting to TOPIC",
                                    queue.getExchangeType(), queue.getExchange());
                        }
                    }

                    channel.exchangeDeclare(queue.getExchange(), type, true); // durable = true

                    if (queue.getQueue() != null && !queue.getQueue().isEmpty()) {
                        channel.queueDeclare(queue.getQueue(), true, false, false, args);
                        channel.queueBind(queue.getQueue(), queue.getExchange(), queue.getRoutingKey());
                    }

                    log.info("Initialized RabbitMQ: Exchange[{}]({}), Queue[{}]",
                            queue.getExchange(), type, queue.getQueue());
                } catch (Exception exception) {
                    log.error("Error initializing RabbitMQ for queue '{}': {}", queue.getQueue(),
                            exception.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Failed to establish RabbitMQ connection for initialization", e);
        }
    }
}
