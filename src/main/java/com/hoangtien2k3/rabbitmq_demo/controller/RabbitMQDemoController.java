package com.hoangtien2k3.rabbitmq_demo.controller;

import com.hoangtien2k3.rabbitmq_demo.common.dto.ApiResponse;
import com.hoangtien2k3.rabbitmq_demo.model.Message;
import com.hoangtien2k3.rabbitmq_demo.model.User;
import com.hoangtien2k3.rabbitmq_demo.service.MessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for RabbitMQ API endpoints
 * Provides endpoints to trigger message production for testing
 */
@Slf4j
@RestController
@RequestMapping("/api/rabbitmq")
@RequiredArgsConstructor
public class RabbitMQDemoController {

    private final MessagingService messagingService;

    // ==================== Direct Exchange - Simple Messages ====================

    @GetMapping("/hello")
    public ResponseEntity<ApiResponse<?>> sendHelloMessage(@RequestParam String message) {
        messagingService.sendHello(message);
        return ResponseEntity.ok(ApiResponse.success("Message sent successfully", message));
    }

    @PostMapping("/user/create")
    public ResponseEntity<ApiResponse<?>> createUser(@RequestBody User user) {
        User createdUser = messagingService.createAndNotifyUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(createdUser));
    }

    // ==================== Direct Exchange - Message Routing ====================

    @PostMapping("/message/normal")
    public ResponseEntity<ApiResponse<?>> sendNormalMessage(@RequestBody Message message) {
        Message sent = messagingService.sendNormalMessage(message);
        return ResponseEntity.accepted().body(ApiResponse.accepted("Normal message sent with ID: " + sent.getId()));
    }

    @PostMapping("/message/critical")
    public ResponseEntity<ApiResponse<?>> sendCriticalMessage(@RequestBody Message message) {
        Message sent = messagingService.sendCriticalMessage(message);
        return ResponseEntity.accepted().body(ApiResponse.accepted("Critical message sent with ID: " + sent.getId()));
    }

    @PostMapping("/message/direct")
    public ResponseEntity<ApiResponse<?>> sendDirectMessage(
            @RequestBody Message message,
            @RequestParam(required = false, defaultValue = "message.info") String routingKey) {
        Message sent = messagingService.sendDirectMessage(message, routingKey);
        return ResponseEntity.accepted().body(ApiResponse.accepted("Direct message sent with ID: " + sent.getId()));
    }

    // ==================== Topic Exchange - Order Events ====================

    @GetMapping("/order/created")
    public ResponseEntity<ApiResponse<?>> publishOrderCreated(@RequestParam String orderId) {
        messagingService.publishOrderCreated(orderId);
        return ResponseEntity.accepted().body(ApiResponse.accepted("Order created event published"));
    }

    @GetMapping("/order/shipped")
    public ResponseEntity<ApiResponse<?>> publishOrderShipped(@RequestParam String orderId) {
        messagingService.publishOrderShipped(orderId);
        return ResponseEntity.accepted().body(ApiResponse.accepted("Order shipped event published"));
    }

    // ==================== Test & Info Endpoints ====================

    @GetMapping("/test-all")
    public ResponseEntity<ApiResponse<?>> testAll() {
        log.info("Executing full test suite");
        messagingService.sendHello("Bulk Test Message");

        User user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .build();
        messagingService.createAndNotifyUser(user);

        Message msg = Message.builder()
                .content("Bulk Content")
                .sender("BulkSender")
                .build();
        messagingService.sendNormalMessage(msg);

        messagingService.publishOrderCreated("ORD-BULK-001");

        return ResponseEntity.ok(ApiResponse.success(
                "Bulk tests triggered successfully. Check logs/queue.",
                "4/4 tests sent"));
    }

    @GetMapping("/info")
    public ResponseEntity<ApiResponse<?>> info() {
        return ResponseEntity.ok(ApiResponse.success("RabbitMQ Demo API",
                "Available exchanges: Direct, Fanout, Topic, Headers"));
    }
}
