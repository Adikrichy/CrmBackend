package org.aldousdev.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {
    private String recipient;
    private String message;
    private String channel; //EMAIL WEBSOKET TELEGRAM
}
