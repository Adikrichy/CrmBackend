package org.aldousdev.analyticsservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID userId;
    private UUID productId;
    private int quantity;
    private BigDecimal salesPrice;
    private BigDecimal costPrice;
    private double totalRevenue;
    private double totalCost;
    private double profit;
    private LocalDate saleDate;
    private UUID orderId;
}