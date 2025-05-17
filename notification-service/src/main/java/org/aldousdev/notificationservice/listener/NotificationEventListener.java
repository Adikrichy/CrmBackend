package org.aldousdev.notificationservice.listener;

import lombok.RequiredArgsConstructor;
import org.aldousdev.notificationservice.model.NotificationEvent;
import org.aldousdev.notificationservice.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService service;

    @RabbitListener(queues = "notifications.queue")
    public void onMessage(NotificationEvent event) {
        service.process(event);
    }
}
