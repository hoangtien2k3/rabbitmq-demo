package com.hoangtien2k3.rabbitmq_demo.common.exception;

public class MessageProcessingException extends RuntimeException {

    private String messageId;
    private String queueName;
    private int retryAttempt;

    public MessageProcessingException(String message) {
        super(message);
    }

    public MessageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageProcessingException(String message, String messageId, String queueName) {
        super(message);
        this.messageId = messageId;
        this.queueName = queueName;
    }

    public MessageProcessingException(String message, String messageId, String queueName, int retryAttempt) {
        super(message);
        this.messageId = messageId;
        this.queueName = queueName;
        this.retryAttempt = retryAttempt;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getQueueName() {
        return queueName;
    }

    public int getRetryAttempt() {
        return retryAttempt;
    }
}
