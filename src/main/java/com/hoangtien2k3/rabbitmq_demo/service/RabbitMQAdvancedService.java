package com.hoangtien2k3.rabbitmq_demo.service;

import com.hoangtien2k3.rabbitmq_demo.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

/**
 * Advanced Service - Xử lý error và retry logic
 * 
 * Kiến thức nâng cao:
 * - Dead Letter Queue (DLQ)
 * - Retry mechanism
 * - Error handling
 * - Message acknowledge
 */
@Service
public class RabbitMQAdvancedService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQAdvancedService.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQAdvancedService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // ==================== RETRY LOGIC ====================

    /**
     * Xử lý message với retry
     * 
     * Nếu xảy ra lỗi, Spring tự động:
     * 1. Lần 1 fail -> Retry sau 1 giây
     * 2. Lần 2 fail -> Retry sau 2 giây (exponential backoff)
     * 3. Lần 3 fail -> Retry sau 4 giây
     * 4. Lần 4+ fail -> Send tới Dead Letter Queue
     * 
     * Cấu hình trong application.yaml:
     * spring.rabbitmq.listener.simple.retry.enabled: true
     * spring.rabbitmq.listener.simple.retry.max-attempts: 3
     * spring.rabbitmq.listener.simple.retry.initial-interval: 1000
     * spring.rabbitmq.listener.simple.retry.multiplier: 2.0
     */

    /**
     * Simulate processing message với khả năng fail
     * 
     * @param message - message để process
     * @param retryCount - số lần đã retry (auto set bởi Spring)
     * @throws Exception - để trigger retry
     */
    public void processMessageWithRetry(Message message, 
                                       @Header(name = AmqpHeaders.REDELIVERED, required = false) Boolean redelivered) {
        try {
            logger.info("🔄 Processing message: {}", message.getId());
            
            // Kiểm tra nếu message đã retry
            if (redelivered != null && redelivered) {
                logger.warn("⚠️  Message đã được redelivery: {}", message.getId());
            }
            
            // Giả lập xử lý
            simulateProcessing(message);
            
            logger.info("✓ Message processed successfully: {}", message.getId());
            
        } catch (Exception e) {
            logger.error("✗ Error processing message: {}, Will retry...", message.getId(), e);
            throw new RuntimeException("Processing failed", e);
        }
    }

    // ==================== ERROR HANDLING ====================

    /**
     * Xử lý message từ Dead Letter Queue
     * 
     * Dead Letter Queue (DLQ) là nơi lưu message:
     * - Fail sau số lần retry tối đa
     * - Cannot deserialize
     * - Explicitly rejected
     * 
     * Thường dùng để:
     * - Logging failures
     * - Manual intervention
     * - Post-mortem analysis
     * 
     * @param message - message fail
     */
    public void handleDeadLetter(Message message) {
        logger.error("💀 [DLQ] Message in Dead Letter Queue: {}", message);
        
        // Log vào database, file, hoặc send alert
        // Example:
        // deadLetterService.log(message);
        // alertService.notifyAdmin("Message failed: " + message.getId());
    }

    /**
     * Xử lý message không thể deserialize
     * 
     * Trường hợp:
     * - Message format sai
     * - Java class không tìm thấy
     * - JSON parse error
     */
    public void handleDeserializationError(String message, Exception exception) {
        logger.error("🔴 Deserialization error: {}, Exception: {}", message, exception.getMessage());
        
        // Handle error
        // Log, alert, or send to DLQ
    }

    // ==================== MESSAGE VALIDATION ====================

    /**
     * Validate message trước khi process
     * 
     * @param message - message cần validate
     * @return true nếu valid, false nếu invalid
     */
    public boolean validateMessage(Message message) {
        logger.info("🔍 Validating message: {}", message.getId());
        
        // Check required fields
        if (message.getId() == null || message.getId().isEmpty()) {
            logger.error("✗ Message ID is required");
            return false;
        }
        
        if (message.getContent() == null || message.getContent().isEmpty()) {
            logger.error("✗ Message content is required");
            return false;
        }
        
        if (message.getSender() == null || message.getSender().isEmpty()) {
            logger.error("✗ Message sender is required");
            return false;
        }
        
        logger.info("✓ Message is valid");
        return true;
    }

    // ==================== BATCH PROCESSING ====================

    /**
     * Process message từ queue một cách batch
     * 
     * Lợi ích:
     * - Xử lý nhiều message cùng lúc
     * - Giảm database round-trips
     * - Tăng performance
     * 
     * @param messages - danh sách message
     */
    public void processBatch(java.util.List<Message> messages) {
        logger.info("📦 Processing batch of {} messages", messages.size());
        
        try {
            // Validate tất cả message trước
            for (Message msg : messages) {
                if (!validateMessage(msg)) {
                    throw new IllegalArgumentException("Invalid message: " + msg.getId());
                }
            }
            
            // Process tất cả
            for (Message msg : messages) {
                simulateProcessing(msg);
            }
            
            // Batch save to database
            // databaseService.saveAll(messages);
            
            logger.info("✓ Batch processed successfully: {} messages", messages.size());
            
        } catch (Exception e) {
            logger.error("✗ Error processing batch: {}", e.getMessage());
            throw new RuntimeException("Batch processing failed", e);
        }
    }

    // ==================== MONITORING ====================

    /**
     * Track message processing metrics
     * 
     * Metrics cần track:
     * - Processing time
     * - Success/failure rate
     * - Queue length
     * - Consumer count
     */
    public void trackMetrics(Message message, long startTime, boolean success) {
        long duration = System.currentTimeMillis() - startTime;
        
        if (success) {
            logger.info("📊 [METRICS] Message processed in {} ms", duration);
        } else {
            logger.warn("📊 [METRICS] Message failed after {} ms", duration);
        }
        
        // Send metrics to monitoring system
        // prometheusRegistry.recordDuration(duration);
        // metricService.recordProcessing(message, duration, success);
    }

    // ==================== CIRCUIT BREAKER PATTERN ====================

    /**
     * Circuit breaker pattern để prevent cascading failures
     * 
     * States:
     * - CLOSED: Normal operation
     * - OPEN: Reject requests khi error rate cao
     * - HALF_OPEN: Test recovery
     * 
     * Sử dụng Spring Cloud Circuit Breaker hoặc Resilience4j
     */
    public void processWithCircuitBreaker(Message message) {
        logger.info("🔌 Processing with circuit breaker: {}", message.getId());
        
        // Spring Cloud Circuit Breaker example:
        // @CircuitBreaker(name = "processMessage", fallbackMethod = "fallback")
        // public void processMessage(Message message) { ... }
        
        // private void fallback(Message message) {
        //     logger.warn("Circuit breaker open, using fallback");
        //     sendToQueue(message, "fallback-queue");
        // }
    }

    // ==================== HELPER METHODS ====================

    /**
     * Giả lập xử lý message
     * 
     * Có thể throw exception để simulate failure
     */
    private void simulateProcessing(Message message) throws InterruptedException {
        logger.info("⏳ Simulating processing...");
        Thread.sleep(500);
        
        // Giả lập 30% chance of failure (comment out để test success)
        // int random = new java.util.Random().nextInt(100);
        // if (random < 30) {
        //     throw new RuntimeException("Random failure simulated");
        // }
        
        logger.info("✓ Processing completed");
    }

    /**
     * Gửi message tới queue khác (fallback)
     */
    public void sendToQueue(Message message, String queueName) {
        logger.info("📤 Sending message to fallback queue: {}", queueName);
        rabbitTemplate.convertAndSend(queueName, message);
    }
}
