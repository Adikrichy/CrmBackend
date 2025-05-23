package org.aldousdev.analyticsservice.service.interfaces;

import org.aldousdev.analyticsservice.entity.DailySummary;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SummaryService {
    List<DailySummary> generateDailySummary(UUID userId, LocalDate date);
    List<DailySummary> getDailySummary(UUID userId, LocalDate date);
    List<DailySummary> getSummaryBetween(UUID userId, LocalDate start, LocalDate end);
}
