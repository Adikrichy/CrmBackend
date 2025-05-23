package org.aldousdev.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummaryResponseDto {
    private UUID userId;
    private LocalDate date;
    private int totalSalesCount;
    private int totalProductsSold;
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal totalProfit;
    private double profitMargin;
}
