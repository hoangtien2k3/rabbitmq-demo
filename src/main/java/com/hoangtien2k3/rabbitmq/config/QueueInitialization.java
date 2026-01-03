package com.hoangtien2k3.rabbitmq.config;

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
    private static final String X_MESSAGE_TTL = "x-message-ttl";

    @Override
    public void afterPropertiesSet() {
        try (Connection connection = connectionFactory.createConnection();
             Channel channel = connection.createChannel(false)) {
            Map<String, Object> args = new HashMap<>();
            args.put(X_MAX_PRIORITY, 100);
            args.put(X_MESSAGE_TTL, 3600000);
            rabbitQueuePropertiesList.forEach(queue -> {
                try {
                    channel.exchangeDeclare(queue.getExchange(), BuiltinExchangeType.TOPIC, false);
                    channel.queueDeclare(queue.getQueue(), true, false, false, args);
                    channel.queueBind(queue.getQueue(), queue.getExchange(), queue.getRoutingKey());
                } catch (Exception exception) {
                    log.error("init channel rabbitmq error {}", exception.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("Failed to establish RabbitMQ connection for initialization", e);
        }
    }
}
