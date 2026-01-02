package com.hoangtien2k3.rabbitmq_demo.model;

import lombok.*;

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
    private static final long serialVersionUID = 1L;

    private String id;
    private String content;
    private String sender;
    private LocalDateTime timestamp;
    private String status;
    private String messageType;
    private Integer priority;

    public Message(String id, String content, String sender) {
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.timestamp = LocalDateTime.now();
        this.status = "PENDING";
    }
}
