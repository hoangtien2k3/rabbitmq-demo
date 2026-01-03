package com.hoangtien2k3.rabbitmq.controller;

import com.hoangtien2k3.rabbitmq.common.dto.ApiResponse;
import com.hoangtien2k3.rabbitmq.model.User;
import com.hoangtien2k3.rabbitmq.service.MessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/rabbitmq")
@RequiredArgsConstructor
public class RabbitMQDemoController {

    private final MessagingService messagingService;

    @GetMapping("/hello")
    public ResponseEntity<ApiResponse<?>> sendHelloMessage(@RequestParam String message) {
        messagingService.sendHello(message);
        return ResponseEntity.ok(ApiResponse.success("Message sent successfully", message));
    }

    @PostMapping("/user/create")
    public ResponseEntity<ApiResponse<?>> createUser(@RequestBody User user) {
        User createdUser = messagingService.createAndNotifyUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(createdUser));
    }
}
