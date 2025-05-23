package org.aldousdev.analyticsservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aldousdev.analyticsservice.entity.Sale;
import org.aldousdev.analyticsservice.service.interfaces.AnalyticsService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyticsEventListener {

    private final AnalyticsService analyticsService;

    @KafkaListener(topics = "sales", groupId = "analytics-service")
    public void handleSaleEvent(Sale sale) {
        log.info("Received sale event for product: {}", sale.getProductId());
        try {
            analyticsService.updateProductAnalytics(sale);
            log.info("Successfully processed sale event for product: {}", sale.getProductId());
        } catch (Exception e) {
            log.error("Error processing sale event for product: {}", sale.getProductId(), e);
        }
    }

    @KafkaListener(topics = "product-views", groupId = "analytics-service")
    public void handleProductView(String productId) {
        log.info("Received product view event for product: {}", productId);
        // Implement product view tracking logic
    }

    @KafkaListener(topics = "cart-updates", groupId = "analytics-service")
    public void handleCartUpdate(String productId) {
        log.info("Received cart update event for product: {}", productId);
        // Implement cart update tracking logic
    }
} 