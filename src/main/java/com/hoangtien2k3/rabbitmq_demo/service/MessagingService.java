package com.hoangtien2k3.rabbitmq_demo.service;

import com.hoangtien2k3.rabbitmq_demo.model.User;
import com.hoangtien2k3.rabbitmq_demo.publisher.topic.HelloTopic;
import com.hoangtien2k3.rabbitmq_demo.publisher.topic.UserTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingService {

    private final UserTopic userTopic;
    private final HelloTopic generalTopic;

    public void sendHello(String text) {
        log.info("Processing hello message: {}", text);
        generalTopic.sendHello(text);
    }

    public User createAndNotifyUser(User user) {
        log.info("Processing user creation: {}", user.getUsername());
        userTopic.sendUserMessage(user);
        return user;
    }
}
