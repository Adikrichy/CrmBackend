package org.aldousdev.mediaservice.repository;

import org.aldousdev.mediaservice.model.MediaFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MediaFileRepository extends MongoRepository<MediaFile, String> {
    List<MediaFile> findByOwnerId(String ownerId);
    List<MediaFile> findByOwnerIdAndType(String ownerId, MediaFile.MediaType type);
    List<MediaFile> findByIsPublicTrue();
    void deleteByOwnerId(String ownerId);
} 