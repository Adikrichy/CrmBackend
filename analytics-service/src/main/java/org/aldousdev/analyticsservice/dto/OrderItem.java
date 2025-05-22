package org.aldousdev.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    private UUID productId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal costPrice;
}
