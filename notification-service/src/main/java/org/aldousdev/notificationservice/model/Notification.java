package org.aldousdev.notificationservice.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue
    private Long id; //email telegram id user id
    private String receiver;
    private String message;
    private String channel; //email telegram webSoket
    private boolean delivered;
    private LocalDateTime timestamp;
}
