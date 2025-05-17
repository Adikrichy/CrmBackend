package org.aldousdev.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.aldousdev.notificationservice.config.RabbitMQConfig;
import org.aldousdev.notificationservice.dto.NotificationRequest;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final AmqpTemplate amqpTemplate;

    @PostMapping
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        amqpTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                request.toEvent()
        );
        return ResponseEntity.ok("Notification sent to queue.");
    }
}

