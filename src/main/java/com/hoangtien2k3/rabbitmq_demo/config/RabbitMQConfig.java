package com.hoangtien2k3.rabbitmq_demo.config;

import com.hoangtien2k3.rabbitmq_demo.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ Configuration
 * 
 * Defines all exchanges, queues, and bindings for the application.
 * 
 * Exchange types:
 * - DIRECT: Routes messages based on exact routing key match
 * - FANOUT: Broadcasts messages to all connected queues
 * - TOPIC: Routes messages based on pattern matching
 * - HEADERS: Routes messages based on header attributes
 */
@Configuration
public class RabbitMQConfig {

    // ==================== Direct Exchange Configuration ====================

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(RabbitMQConstant.EXCHANGE_DIRECT, true, false);
    }

    @Bean
    public Queue helloQueue() {
        return QueueBuilder.durable(RabbitMQConstant.QUEUE_HELLO)
                .withArgument("x-message-ttl", 3600000)
                .build();
    }

    @Bean
    public Queue userQueue() {
        return QueueBuilder.durable(RabbitMQConstant.QUEUE_USER)
                .withArgument("x-message-ttl", 3600000)
                .build();
    }

    @Bean
    public Queue messageNormalQueue() {
        return QueueBuilder.durable(RabbitMQConstant.QUEUE_MESSAGE_NORMAL)
                .withArgument("x-message-ttl", 1800000)
                .build();
    }

    @Bean
    public Queue messageCriticalQueue() {
        return QueueBuilder.durable(RabbitMQConstant.QUEUE_MESSAGE_CRITICAL)
                .withArgument("x-message-ttl", 7200000)
                .build();
    }

    @Bean
    public Binding helloBinding() {
        return BindingBuilder.bind(helloQueue())
                .to(directExchange())
                .with(RabbitMQConstant.ROUTING_KEY_HELLO);
    }

    @Bean
    public Binding messageNormalBinding() {
        return BindingBuilder.bind(messageNormalQueue())
                .to(directExchange())
                .with(RabbitMQConstant.ROUTING_KEY_MESSAGE_INFO);
    }

    @Bean
    public Binding messageCriticalBinding() {
        return BindingBuilder.bind(messageCriticalQueue())
                .to(directExchange())
                .with(RabbitMQConstant.ROUTING_KEY_MESSAGE_ERROR);
    }

    // ==================== Topic Exchange Configuration ====================

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(RabbitMQConstant.EXCHANGE_TOPIC, true, false);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return QueueBuilder.durable(RabbitMQConstant.QUEUE_ORDER_CREATED)
                .withArgument("x-message-ttl", 7200000)
                .build();
    }

    @Bean
    public Queue orderShippedQueue() {
        return QueueBuilder.durable(RabbitMQConstant.QUEUE_ORDER_SHIPPED)
                .withArgument("x-message-ttl", 7200000)
                .build();
    }

    @Bean
    public Binding orderCreatedBinding() {
        return BindingBuilder.bind(orderCreatedQueue())
                .to(topicExchange())
                .with(RabbitMQConstant.ROUTING_KEY_ORDER_CREATED);
    }

    @Bean
    public Binding orderShippedBinding() {
        return BindingBuilder.bind(orderShippedQueue())
                .to(topicExchange())
                .with(RabbitMQConstant.ROUTING_KEY_ORDER_SHIPPED);
    }

    // ==================== Fanout Exchange Configuration ====================

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(RabbitMQConstant.EXCHANGE_FANOUT, true, false);
    }

    @Bean
    public Queue headersQueue() {
        return QueueBuilder.durable(RabbitMQConstant.QUEUE_HEADERS)
                .withArgument("x-message-ttl", 3600000)
                .build();
    }

    @Bean
    public Binding headersBinding() {
        return BindingBuilder.bind(headersQueue())
                .to(fanoutExchange());
    }

    // ==================== Headers Exchange Configuration ====================

    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(RabbitMQConstant.EXCHANGE_HEADERS, true, false);
    }
}
