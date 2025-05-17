package org.aldousdev.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aldousdev.notificationservice.model.NotificationEvent;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private String recipient;
    private String message;
    private String channel; // "EMAIL", "TELEGRAM", "WEBSOCKET"

    public NotificationEvent toEvent() {
        return NotificationEvent.builder()
                .recipient(this.recipient)
                .message(this.message)
                .channel(this.channel)
                .build();
    }
}
