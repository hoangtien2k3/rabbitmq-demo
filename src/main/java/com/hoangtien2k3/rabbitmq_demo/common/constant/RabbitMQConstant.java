package com.hoangtien2k3.rabbitmq_demo.common.constant;

public class RabbitMQConstant {

    // ==================== Exchange Names ====================
    public static final String EXCHANGE_DIRECT = "direct.exchange";
    public static final String EXCHANGE_FANOUT = "fanout.exchange";
    public static final String EXCHANGE_TOPIC = "topic.exchange";
    public static final String EXCHANGE_HEADERS = "headers.exchange";

    // ==================== Queue Names ====================
    public static final String QUEUE_HELLO = "hello.queue";
    public static final String QUEUE_USER = "user.queue";
    public static final String QUEUE_MESSAGE_NORMAL = "message.normal.queue";
    public static final String QUEUE_MESSAGE_CRITICAL = "message.critical.queue";
    public static final String QUEUE_ORDER_CREATED = "order.created.queue";
    public static final String QUEUE_ORDER_SHIPPED = "order.shipped.queue";
    public static final String QUEUE_HEADERS = "headers.queue";

    // ==================== Routing Keys ====================
    public static final String ROUTING_KEY_HELLO = "hello";
    public static final String ROUTING_KEY_USER = "user.#";
    public static final String ROUTING_KEY_MESSAGE_INFO = "message.info";
    public static final String ROUTING_KEY_MESSAGE_ERROR = "message.error";
    public static final String ROUTING_KEY_ORDER_CREATED = "order.created";
    public static final String ROUTING_KEY_ORDER_SHIPPED = "order.shipped";

    // ==================== Message Types ====================
    public static final String MESSAGE_TYPE_HELLO = "HELLO";
    public static final String MESSAGE_TYPE_USER = "USER";
    public static final String MESSAGE_TYPE_ORDER = "ORDER";
    public static final String MESSAGE_TYPE_NOTIFICATION = "NOTIFICATION";

    // ==================== Message Status ====================
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_PROCESSING = "PROCESSING";
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_RETRYING = "RETRYING";

    // ==================== Message Priority ====================
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_NORMAL = 5;
    public static final int PRIORITY_HIGH = 10;

    // ==================== Retry Settings ====================
    public static final int MAX_RETRY_ATTEMPTS = 3;
    public static final long RETRY_DELAY_MS = 1000;
    public static final double RETRY_MULTIPLIER = 2.0;
    public static final long MAX_RETRY_DELAY_MS = 10000;

    // ==================== Timeout Settings ====================
    public static final long MESSAGE_TIMEOUT_MS = 30000;
    public static final long PROCESSING_TIMEOUT_MS = 60000;

    // ==================== Headers ====================
    public static final String HEADER_MESSAGE_ID = "x-message-id";
    public static final String HEADER_TIMESTAMP = "x-timestamp";
    public static final String HEADER_RETRY_COUNT = "x-retry-count";
    public static final String HEADER_ORIGINAL_QUEUE = "x-original-queue";
    public static final String HEADER_ERROR_MESSAGE = "x-error-message";
    public static final String HEADER_TRACE_ID = "x-trace-id";

    private RabbitMQConstant() {
        throw new AssertionError("Cannot instantiate constant class");
    }
}
