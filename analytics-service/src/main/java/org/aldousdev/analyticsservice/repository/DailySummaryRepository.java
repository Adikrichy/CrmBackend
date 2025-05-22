package org.aldousdev.analyticsservice.repository;

import org.aldousdev.analyticsservice.entity.DailySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
public interface DailySummaryRepository extends JpaRepository<DailySummary, UUID> {
    List<DailySummary> findByUserId(UUID userId);
}
