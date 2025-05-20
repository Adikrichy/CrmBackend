package org.aldousdev.mediaservice.controller;

import lombok.RequiredArgsConstructor;
import org.aldousdev.mediaservice.model.MediaFile;
import org.aldousdev.mediaservice.service.MediaService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;

    @PostMapping("/upload")
    public ResponseEntity<MediaFile> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("ownerId") String ownerId,
            @RequestParam(value = "isPublic", defaultValue = "false") boolean isPublic) throws IOException {
        return ResponseEntity.ok(mediaService.uploadFile(file, ownerId, isPublic));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String id) throws IOException {
        MediaFile mediaFile = mediaService.getFilesByOwner(id).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("File not found"));

        byte[] data = mediaService.downloadFile(id);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + mediaFile.getFilename())
                .contentType(MediaType.parseMediaType(mediaFile.getContentType()))
                .contentLength(data.length)
                .body(resource);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<MediaFile>> getFilesByOwner(@PathVariable String ownerId) {
        return ResponseEntity.ok(mediaService.getFilesByOwner(ownerId));
    }

    @GetMapping("/public")
    public ResponseEntity<List<MediaFile>> getPublicFiles() {
        return ResponseEntity.ok(mediaService.getPublicFiles());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable String id) {
        mediaService.deleteFile(id);
        return ResponseEntity.ok().build();
    }
} 