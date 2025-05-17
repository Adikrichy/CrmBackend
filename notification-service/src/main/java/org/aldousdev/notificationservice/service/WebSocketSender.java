package org.aldousdev.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketSender {
    private final SimpMessagingTemplate messagingTemplate;

    public boolean send(String userId, String message) {
        try {
            messagingTemplate.convertAndSend("/topic/notifications/" + userId, message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

