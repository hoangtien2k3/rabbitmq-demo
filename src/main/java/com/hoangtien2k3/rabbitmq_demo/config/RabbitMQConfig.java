package com.hoangtien2k3.rabbitmq_demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final RabbitQueueProperties rabbitQueueProperties;

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
     * Expose the list of queues for QueueInitialization by extracting it from the
     * properties bean.
     */
    @Bean
    public List<RabbitQueueProperties> rabbitQueuePropertiesList() {
        if (rabbitQueueProperties.getQueues() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(rabbitQueueProperties.getQueues().values());
    }
}
