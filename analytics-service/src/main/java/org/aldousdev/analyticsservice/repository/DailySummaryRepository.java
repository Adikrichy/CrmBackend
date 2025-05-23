package org.aldousdev.analyticsservice.repository;

import org.aldousdev.analyticsservice.entity.DailySummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface DailySummaryRepository extends JpaRepository<DailySummary, UUID> {
    Optional<DailySummary> findByUserIdAndDate(UUID userId, LocalDate date);
    List<DailySummary> findByUserIdAndDateBetween(UUID userId, LocalDate start, LocalDate end);
}
