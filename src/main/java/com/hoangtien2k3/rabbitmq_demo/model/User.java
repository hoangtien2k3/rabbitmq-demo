package com.hoangtien2k3.rabbitmq_demo.model;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * User model for event-driven architecture demo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;
    private String status;
    private String phoneNumber;
}
