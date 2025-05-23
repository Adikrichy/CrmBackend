package org.aldousdev.analyticsservice.controller;

import lombok.RequiredArgsConstructor;
import org.aldousdev.analyticsservice.dto.ProductAnalyticsDTO;
import org.aldousdev.analyticsservice.entity.ProductAnalytics;
import org.aldousdev.analyticsservice.mapper.ProductAnalyticsMapper;
import org.aldousdev.analyticsservice.service.interfaces.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final ProductAnalyticsMapper productAnalyticsMapper;

    @GetMapping("/products/top-selling")
    public ResponseEntity<List<ProductAnalyticsDTO>> getTopSellingProducts() {
        List<ProductAnalyticsDTO> dtos = analyticsService.getTopSellingProducts().stream()
                .map(productAnalyticsMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/products/best-converting")
    public ResponseEntity<List<ProductAnalyticsDTO>> getBestConvertingProducts() {
        List<ProductAnalyticsDTO> dtos = analyticsService.getBestConvertingProducts().stream()
                .map(productAnalyticsMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/products/low-stock")
    public ResponseEntity<List<ProductAnalyticsDTO>> getLowStockProducts(
            @RequestParam(defaultValue = "10") int threshold) {
        List<ProductAnalyticsDTO> dtos = analyticsService.getLowStockProducts(threshold).stream()
                .map(productAnalyticsMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/products/most-viewed")
    public ResponseEntity<List<ProductAnalyticsDTO>> getMostViewedProducts() {
        List<ProductAnalyticsDTO> dtos = analyticsService.getMostViewedProducts().stream()
                .map(productAnalyticsMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductAnalyticsDTO> getProductAnalytics(@PathVariable UUID productId) {
        ProductAnalytics analytics = analyticsService.getProductAnalytics(productId);
        return analytics != null 
            ? ResponseEntity.ok(productAnalyticsMapper.toDTO(analytics))
            : ResponseEntity.notFound().build();
    }

    @GetMapping("/revenue")
    public ResponseEntity<BigDecimal> getTotalRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(analyticsService.getTotalRevenue(startDate, endDate));
    }

    @GetMapping("/profit")
    public ResponseEntity<BigDecimal> getTotalProfit(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(analyticsService.getTotalProfit(startDate, endDate));
    }

    @GetMapping("/sales")
    public ResponseEntity<Integer> getTotalSales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(analyticsService.getTotalSales(startDate, endDate));
    }
} 