package com.hoangtien2k3.rabbitmq_demo.common.util;

import java.util.UUID;

public class MessageIdGenerator {

    public static String generateMessageId() {
        return UUID.randomUUID().toString();
    }

    public static String generateMessageId(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString();
    }

    public static String generateOrderId() {
        return "ORD_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String generateUserId() {
        return "USR_" + System.currentTimeMillis();
    }

    public static String generateTraceId() {
        return "TRACE_" + UUID.randomUUID().toString();
    }
}
