package org.aldousdev.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.aldousdev.notificationservice.model.NotificationEvent;
import org.aldousdev.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;
    private final EmailSender emailSender;
    private final TelegramSender telegramSender;
    private final WebSocketSender webSocketSender;

    public void process(NotificationEvent event) {
        boolean success = switch (event.getChannel().toUpperCase()) {
            case "EMAIL" -> emailSender.send(event.getRecipient(), event.getMessage());
            case "TELEGRAM" -> telegramSender.send(event.getRecipient(), event.getMessage());
            case "WEBSOCKET" -> webSocketSender.send(event.getRecipient(), event.getMessage());
            default -> false;
        };

        repository.save(Notification.builder()
                .recipient(event.getRecipient())
                .message(event.getMessage())
                .channel(event.getChannel())
                .delivered(success)
                .timestamp(LocalDateTime.now())
                .build());
    }
}

