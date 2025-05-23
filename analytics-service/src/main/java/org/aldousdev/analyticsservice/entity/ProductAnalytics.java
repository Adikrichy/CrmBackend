package org.aldousdev.analyticsservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "product_analytics")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private UUID productId;
    private String productName;
    private int totalSales;
    private BigDecimal totalRevenue;
    private BigDecimal totalProfit;
    private int stockLevel;
    private double averageRating;
    private int totalReviews;
    private LocalDateTime lastUpdated;
    
    @Column(name = "view_count")
    private int viewCount;
    
    @Column(name = "cart_additions")
    private int cartAdditions;
    
    @Column(name = "purchase_conversion_rate")
    private double purchaseConversionRate;
} 