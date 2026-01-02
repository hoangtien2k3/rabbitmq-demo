package com.hoangtien2k3.rabbitmq_demo.controller;

import com.hoangtien2k3.rabbitmq_demo.common.dto.ApiResponse;
import com.hoangtien2k3.rabbitmq_demo.common.util.MessageIdGenerator;
import com.hoangtien2k3.rabbitmq_demo.model.Message;
import com.hoangtien2k3.rabbitmq_demo.model.User;
import com.hoangtien2k3.rabbitmq_demo.producer.RabbitMQProducer;
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

    private final RabbitMQProducer producer;

    // ==================== Direct Exchange - Simple Messages ====================

    @GetMapping("/hello")
    public ResponseEntity<ApiResponse<?>> sendHelloMessage(@RequestParam String message) {
        log.info("Endpoint called: sendHelloMessage - {}", message);
        try {
            producer.sendHelloMessage(message);
            return ResponseEntity.ok(ApiResponse.success("Message sent successfully", message));
        } catch (Exception e) {
            log.error("Error sending hello message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Error sending message: " + e.getMessage()));
        }
    }

    @PostMapping("/user/create")
    public ResponseEntity<ApiResponse<?>> createUser(@RequestBody User user) {
        log.info("Endpoint called: createUser - {}", user.getUsername());
        try {
            if (user.getId() == null) {
                user.setId(System.currentTimeMillis());
            }
            producer.sendUserMessage(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.created(user));
        } catch (Exception e) {
            log.error("Error creating user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Error creating user: " + e.getMessage()));
        }
    }

    // ==================== Direct Exchange - Message Routing ====================

    @PostMapping("/message/normal")
    public ResponseEntity<ApiResponse<?>> sendNormalMessage(@RequestBody Message message) {
        log.info("Endpoint called: sendNormalMessage");
        try {
            if (message.getId() == null) {
                message.setId(MessageIdGenerator.generateMessageId("MSG"));
            }
            producer.sendNormalMessage(message);
            return ResponseEntity.accepted().body(ApiResponse.accepted("Normal message sent"));
        } catch (Exception e) {
            log.error("Error sending normal message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Error sending normal message"));
        }
    }

    @PostMapping("/message/critical")
    public ResponseEntity<ApiResponse<?>> sendCriticalMessage(@RequestBody Message message) {
        log.info("Endpoint called: sendCriticalMessage");
        try {
            if (message.getId() == null) {
                message.setId(MessageIdGenerator.generateMessageId("CRITICAL"));
            }
            producer.sendCriticalMessage(message);
            return ResponseEntity.accepted().body(ApiResponse.accepted("Critical message sent"));
        } catch (Exception e) {
            log.error("Error sending critical message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Error sending critical message"));
        }
    }

    @PostMapping("/message/direct")
    public ResponseEntity<ApiResponse<?>> sendDirectMessage(
            @RequestBody Message message,
            @RequestParam(required = false, defaultValue = "message.info") String routingKey) {
        log.info("Endpoint called: sendDirectMessage with routing key: {}", routingKey);
        try {
            if (message.getId() == null) {
                message.setId(MessageIdGenerator.generateMessageId());
            }
            producer.sendDirectMessage(message, routingKey);
            return ResponseEntity.accepted().body(ApiResponse.accepted("Direct message sent"));
        } catch (Exception e) {
            log.error("Error sending direct message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Error sending direct message"));
        }
    }

    // ==================== Topic Exchange - Order Events ====================

    @GetMapping("/order/created")
    public ResponseEntity<ApiResponse<?>> publishOrderCreated(@RequestParam String orderId) {
        log.info("Endpoint called: publishOrderCreated - {}", orderId);
        try {
            String message = "Order created: " + orderId;
            producer.publishOrderCreatedEvent(orderId, message);
            return ResponseEntity.accepted().body(ApiResponse.accepted("Order created event published"));
        } catch (Exception e) {
            log.error("Error publishing order created event", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Error publishing order created event"));
        }
    }

    @GetMapping("/order/shipped")
    public ResponseEntity<ApiResponse<?>> publishOrderShipped(@RequestParam String orderId) {
        log.info("Endpoint called: publishOrderShipped - {}", orderId);
        try {
            String message = "Order shipped: " + orderId;
            producer.publishOrderShippedEvent(orderId, message);
            return ResponseEntity.accepted().body(ApiResponse.accepted("Order shipped event published"));
        } catch (Exception e) {
            log.error("Error publishing order shipped event", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Error publishing order shipped event"));
        }
    }

    @PostMapping("/order/event")
    public ResponseEntity<ApiResponse<?>> publishOrderEvent(
            @RequestParam(defaultValue = "order.created") String routingKey,
            @RequestBody String message) {
        log.info("Endpoint called: publishOrderEvent with routing key: {}", routingKey);
        try {
            producer.publishOrderEvent(message, routingKey);
            return ResponseEntity.accepted().body(ApiResponse.accepted("Order event published"));
        } catch (Exception e) {
            log.error("Error publishing order event", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Error publishing order event"));
        }
    }

    // ==================== Fanout Exchange - Broadcasting ====================

    @GetMapping("/headers")
    public ResponseEntity<ApiResponse<?>> sendHeadersMessage(@RequestParam String message) {
        log.info("Endpoint called: sendHeadersMessage");
        try {
            producer.publishHeadersMessage(message);
            return ResponseEntity.accepted().body(ApiResponse.accepted("Headers message sent"));
        } catch (Exception e) {
            log.error("Error sending headers message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Error sending headers message"));
        }
    }

    // ==================== Test & Info Endpoints ====================

    @GetMapping("/test-all")
    public ResponseEntity<ApiResponse<?>> testAll() {
        log.info("Endpoint called: testAll");
        try {
            producer.sendHelloMessage("Test message");
            Thread.sleep(500);

            User user = User.builder()
                    .id(System.currentTimeMillis())
                    .username("testuser")
                    .email("test@example.com")
                    .build();
            producer.sendUserMessage(user);
            Thread.sleep(500);

            Message msg = Message.builder()
                    .id(MessageIdGenerator.generateMessageId())
                    .content("Test message")
                    .sender("TestUser")
                    .build();
            producer.sendNormalMessage(msg);
            Thread.sleep(500);

            producer.publishOrderCreatedEvent("ORD-001", "Order created");
            Thread.sleep(500);

            return ResponseEntity.ok(ApiResponse.success(
                    "All tests completed successfully. Check logs for results.",
                    "4 tests sent"));
        } catch (InterruptedException e) {
            log.error("Error during test-all", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.internalError("Error during testing"));
        }
    }

    @GetMapping("/info")
    public ResponseEntity<ApiResponse<?>> info() {
        return ResponseEntity.ok(ApiResponse.success("RabbitMQ Demo API",
                "See /api/rabbitmq/swagger-ui.html for full documentation"));
    }
}
