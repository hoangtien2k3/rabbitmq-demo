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

    public static final String HEADER_TRACE_ID = "x-trace-id";

    private RabbitMQConstant() {
        throw new AssertionError("Cannot instantiate constant class");
    }
}
