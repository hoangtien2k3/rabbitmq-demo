package com.hoangtien2k3.rabbitmq_demo.common.util;

import java.util.UUID;

public class MessageIdGenerator {

    public static String generateMessageId(String prefix) {
        return prefix + "_" + UUID.randomUUID();
    }

    public static String generateTraceId() {
        return "TRACE_" + UUID.randomUUID();
    }
}
