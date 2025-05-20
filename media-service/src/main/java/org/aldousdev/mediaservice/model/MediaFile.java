package org.aldousdev.mediaservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "media_files")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaFile {
    @Id
    private String id;
    
    private String filename;
    private String contentType;
    private long size;
    private String gridFsId;
    private String url;
    private String ownerId;
    private MediaType type;
    private LocalDateTime uploadedAt;
    private boolean isPublic;
    
    public enum MediaType {
        IMAGE,
        VIDEO,
        DOCUMENT
    }
} 