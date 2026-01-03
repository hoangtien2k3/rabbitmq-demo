package com.hoangtien2k3.rabbitmq_demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    /**
     * Map configuration properties directly from application.yaml
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.rabbitmq.queues")
    public Map<String, RabbitQueueProperties> rabbitQueuesMap() {
        return new HashMap<>();
    }

    /**
     * Expose the list of queues for QueueInitialization
     */
    @Bean
    public List<RabbitQueueProperties> rabbitQueuePropertiesList(Map<String, RabbitQueueProperties> rabbitQueuesMap) {
        return new ArrayList<>(rabbitQueuesMap.values());
    }
}
