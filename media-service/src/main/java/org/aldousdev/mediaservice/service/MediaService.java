package org.aldousdev.mediaservice.service;

import lombok.RequiredArgsConstructor;
import org.aldousdev.mediaservice.model.MediaFile;
import org.aldousdev.mediaservice.repository.MediaFileRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final GridFsTemplate gridFsTemplate;
    private final MediaFileRepository mediaFileRepository;

    public MediaFile uploadFile(MultipartFile file, String ownerId, boolean isPublic) throws IOException {
        String gridFsId = gridFsTemplate.store(
            file.getInputStream(),
            file.getOriginalFilename(),
            file.getContentType()
        ).toString();

        MediaFile.MediaType type = determineMediaType(file.getContentType());
        
        MediaFile mediaFile = MediaFile.builder()
            .filename(file.getOriginalFilename())
            .contentType(file.getContentType())
            .size(file.getSize())
            .gridFsId(gridFsId)
            .url("/api/v1/media/" + gridFsId)
            .ownerId(ownerId)
            .type(type)
            .uploadedAt(LocalDateTime.now())
            .isPublic(isPublic)
            .build();

        return mediaFileRepository.save(mediaFile);
    }

    public byte[] downloadFile(String id) throws IOException {
        MediaFile mediaFile = mediaFileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("File not found"));
        
        return gridFsTemplate.getResource(mediaFile.getGridFsId()).getInputStream().readAllBytes();
    }

    public List<MediaFile> getFilesByOwner(String ownerId) {
        return mediaFileRepository.findByOwnerId(ownerId);
    }

    public List<MediaFile> getPublicFiles() {
        return mediaFileRepository.findByIsPublicTrue();
    }

    public void deleteFile(String id) {
        MediaFile mediaFile = mediaFileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("File not found"));
        
        Query query = new Query(Criteria.where("_id").is(mediaFile.getGridFsId()));
        gridFsTemplate.delete(query);
        mediaFileRepository.deleteById(id);
    }

    private MediaFile.MediaType determineMediaType(String contentType) {
        if (contentType.startsWith("image/")) {
            return MediaFile.MediaType.IMAGE;
        } else if (contentType.startsWith("video/")) {
            return MediaFile.MediaType.VIDEO;
        } else {
            return MediaFile.MediaType.DOCUMENT;
        }
    }
} 