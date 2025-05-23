package org.aldousdev.analyticsservice.service.interfaces;

import org.aldousdev.analyticsservice.dto.OrderCreatedEvent;
import org.aldousdev.analyticsservice.entity.Sale;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SalesService {
    void handleOrderCreatedEvent(OrderCreatedEvent event);
    List<Sale> getSalesByUserId(UUID userId);
    List<Sale> getSalesByUserIdAndDate(UUID userId,LocalDate date);
    void deleteSalesByUserId(UUID userId);
}
