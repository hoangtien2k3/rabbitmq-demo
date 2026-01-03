package com.hoangtien2k3.rabbitmq_demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@NoArgsConstructor
public class User implements Serializable {
    private String username;
    private String email;
    private String status;
    private String phoneNumber;
}
