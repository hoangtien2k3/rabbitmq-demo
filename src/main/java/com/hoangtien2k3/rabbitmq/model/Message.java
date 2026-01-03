package com.hoangtien2k3.rabbitmq.model;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Message model for RabbitMQ communication
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String content;
    private String sender;
    private LocalDateTime timestamp;
    private String status;
    private String messageType;
    private Integer priority;
}
