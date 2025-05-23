package org.aldousdev.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAnalyticsDTO {
    private UUID productId;
    private String productName;
    private int totalSales;
    private BigDecimal totalRevenue;
    private BigDecimal totalProfit;
    private int stockLevel;
    private double averageRating;
    private int totalReviews;
    private LocalDateTime lastUpdated;
    private int viewCount;
    private int cartAdditions;
    private double purchaseConversionRate;
} 