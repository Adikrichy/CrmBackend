package org.aldousdev.analyticsservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aldousdev.analyticsservice.dto.ProductAnalyticsDTO;
import org.aldousdev.analyticsservice.entity.ProductAnalytics;
import org.aldousdev.analyticsservice.entity.Sale;
import org.aldousdev.analyticsservice.exception.AnalyticsException;
import org.aldousdev.analyticsservice.mapper.ProductAnalyticsMapper;
import org.aldousdev.analyticsservice.repository.ProductAnalyticsRepository;
import org.aldousdev.analyticsservice.repository.SaleRepository;
import org.aldousdev.analyticsservice.service.interfaces.AnalyticsService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final ProductAnalyticsRepository productAnalyticsRepository;
    private final SaleRepository saleRepository;
    private final ProductAnalyticsMapper productAnalyticsMapper;

    @Override
    @Transactional
    @CacheEvict(value = {"topSellingProducts", "bestConvertingProducts", "trendingProducts"}, allEntries = true)
    public void updateProductAnalytics(Sale sale) {
        try {
            ProductAnalytics analytics = productAnalyticsRepository.findByProductId(sale.getProductId());
            
            if (analytics == null) {
                analytics = ProductAnalytics.builder()
                        .productId(sale.getProductId())
                        .totalSales(0)
                        .totalRevenue(BigDecimal.ZERO)
                        .totalProfit(BigDecimal.ZERO)
                        .viewCount(0)
                        .cartAdditions(0)
                        .purchaseConversionRate(0.0)
                        .build();
            }

            analytics.setTotalSales(analytics.getTotalSales() + sale.getQuantity());
            analytics.setTotalRevenue(analytics.getTotalRevenue().add(BigDecimal.valueOf(sale.getTotalRevenue())));
            analytics.setTotalProfit(analytics.getTotalProfit().add(BigDecimal.valueOf(sale.getProfit())));
            analytics.setLastUpdated(java.time.LocalDateTime.now());
            
            updateConversionRate(analytics);
            productAnalyticsRepository.save(analytics);
            
            log.info("Successfully updated analytics for product: {}", sale.getProductId());
        } catch (Exception e) {
            log.error("Error updating product analytics for product: {}", sale.getProductId(), e);
            throw new AnalyticsException("Failed to update product analytics", e);
        }
    }

    @Override
    @Cacheable(value = "productAnalytics", key = "#productId")
    public ProductAnalyticsDTO getProductAnalytics(UUID productId) {
        try {
            ProductAnalytics analytics = productAnalyticsRepository.findByProductId(productId);
            if (analytics == null) {
                throw new AnalyticsException("Product analytics not found for product: " + productId);
            }
            return productAnalyticsMapper.toDTO(analytics);
        } catch (Exception e) {
            log.error("Error retrieving product analytics for product: {}", productId, e);
            throw new AnalyticsException("Failed to retrieve product analytics", e);
        }
    }

    @Override
    @Cacheable(value = "topSellingProducts")
    public List<ProductAnalyticsDTO> getTopSellingProducts() {
        try {
            return productAnalyticsRepository.findTopSellingProducts().stream()
                    .map(productAnalyticsMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving top selling products", e);
            throw new AnalyticsException("Failed to retrieve top selling products", e);
        }
    }

    @Override
    @Cacheable(value = "bestConvertingProducts")
    public List<ProductAnalyticsDTO> getBestConvertingProducts() {
        try {
            return productAnalyticsRepository.findBestConvertingProducts().stream()
                    .map(productAnalyticsMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving best converting products", e);
            throw new AnalyticsException("Failed to retrieve best converting products", e);
        }
    }

    @Override
    @Cacheable(value = "lowStockProducts")
    public List<ProductAnalyticsDTO> getLowStockProducts(int threshold) {
        try {
            return productAnalyticsRepository.findLowStockProducts(threshold).stream()
                    .map(productAnalyticsMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving low stock products", e);
            throw new AnalyticsException("Failed to retrieve low stock products", e);
        }
    }

    @Override
    @Cacheable(value = "mostViewedProducts")
    public List<ProductAnalyticsDTO> getMostViewedProducts() {
        try {
            return productAnalyticsRepository.findMostViewedProducts().stream()
                    .map(productAnalyticsMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving most viewed products", e);
            throw new AnalyticsException("Failed to retrieve most viewed products", e);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"mostViewedProducts"}, allEntries = true)
    public void updateProductView(UUID productId) {
        try {
            ProductAnalytics analytics = productAnalyticsRepository.findByProductId(productId);
            if (analytics != null) {
                analytics.setViewCount(analytics.getViewCount() + 1);
                analytics.setLastUpdated(java.time.LocalDateTime.now());
                updateConversionRate(analytics);
                productAnalyticsRepository.save(analytics);
                log.info("Updated view count for product: {}", productId);
            }
        } catch (Exception e) {
            log.error("Error updating product view for product: {}", productId, e);
            throw new AnalyticsException("Failed to update product view", e);
        }
    }

    @Override
    @Transactional
    public void updateCartAddition(UUID productId) {
        try {
            ProductAnalytics analytics = productAnalyticsRepository.findByProductId(productId);
            if (analytics != null) {
                analytics.setCartAdditions(analytics.getCartAdditions() + 1);
                analytics.setLastUpdated(java.time.LocalDateTime.now());
                productAnalyticsRepository.save(analytics);
                log.info("Updated cart additions for product: {}", productId);
            }
        } catch (Exception e) {
            log.error("Error updating cart addition for product: {}", productId, e);
            throw new AnalyticsException("Failed to update cart addition", e);
        }
    }

    @Override
    public BigDecimal getTotalRevenue(LocalDate startDate, LocalDate endDate) {
        try {
            return saleRepository.getTotalRevenueBetweenDates(startDate, endDate);
        } catch (Exception e) {
            log.error("Error calculating total revenue", e);
            throw new AnalyticsException("Failed to calculate total revenue", e);
        }
    }

    @Override
    public BigDecimal getTotalProfit(LocalDate startDate, LocalDate endDate) {
        try {
            return saleRepository.getTotalProfitBetweenDates(startDate, endDate);
        } catch (Exception e) {
            log.error("Error calculating total profit", e);
            throw new AnalyticsException("Failed to calculate total profit", e);
        }
    }

    @Override
    public int getTotalSales(LocalDate startDate, LocalDate endDate) {
        try {
            return saleRepository.getTotalSalesBetweenDates(startDate, endDate);
        } catch (Exception e) {
            log.error("Error calculating total sales", e);
            throw new AnalyticsException("Failed to calculate total sales", e);
        }
    }

    @Override
    public BigDecimal getAverageOrderValue(LocalDate startDate, LocalDate endDate) {
        try {
            BigDecimal totalRevenue = getTotalRevenue(startDate, endDate);
            int totalSales = getTotalSales(startDate, endDate);
            
            if (totalSales == 0) {
                return BigDecimal.ZERO;
            }
            
            return totalRevenue.divide(BigDecimal.valueOf(totalSales), 2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("Error calculating average order value", e);
            throw new AnalyticsException("Failed to calculate average order value", e);
        }
    }

    @Override
    @Cacheable(value = "trendingProducts")
    public List<ProductAnalyticsDTO> getTrendingProducts(int limit) {
        try {
            return productAnalyticsRepository.findTrendingProducts(limit).stream()
                    .map(productAnalyticsMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving trending products", e);
            throw new AnalyticsException("Failed to retrieve trending products", e);
        }
    }

    @Override
    @Cacheable(value = "productsByCategory")
    public List<ProductAnalyticsDTO> getProductsByCategory(String category) {
        try {
            return productAnalyticsRepository.findByCategory(category).stream()
                    .map(productAnalyticsMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving products by category: {}", category, e);
            throw new AnalyticsException("Failed to retrieve products by category", e);
        }
    }

    @Override
    @Cacheable(value = "highestProfitMarginProducts")
    public List<ProductAnalyticsDTO> getHighestProfitMarginProducts(int limit) {
        try {
            return productAnalyticsRepository.findHighestProfitMarginProducts(limit).stream()
                    .map(productAnalyticsMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving highest profit margin products", e);
            throw new AnalyticsException("Failed to retrieve highest profit margin products", e);
        }
    }

    @Override
    @Cacheable(value = "highestRatedProducts")
    public List<ProductAnalyticsDTO> getHighestRatedProducts(int limit) {
        try {
            return productAnalyticsRepository.findHighestRatedProducts(limit).stream()
                    .map(productAnalyticsMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving highest rated products", e);
            throw new AnalyticsException("Failed to retrieve highest rated products", e);
        }
    }

    @Override
    @Cacheable(value = "productsNeedingRestock")
    public List<ProductAnalyticsDTO> getProductsNeedingRestock(int daysToStockout) {
        try {
            return productAnalyticsRepository.findProductsNeedingRestock(daysToStockout).stream()
                    .map(productAnalyticsMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving products needing restock", e);
            throw new AnalyticsException("Failed to retrieve products needing restock", e);
        }
    }

    @Override
    public double getSalesVelocity(UUID productId, int days) {
        try {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusDays(days);
            
            int totalSales = saleRepository.getTotalSalesForProductBetweenDates(productId, startDate, endDate);
            return (double) totalSales / days;
        } catch (Exception e) {
            log.error("Error calculating sales velocity for product: {}", productId, e);
            throw new AnalyticsException("Failed to calculate sales velocity", e);
        }
    }

    @Override
    public double getCustomerRetentionRate(LocalDate startDate, LocalDate endDate) {
        try {
            int totalCustomers = saleRepository.getTotalCustomersBetweenDates(startDate, endDate);
            int returningCustomers = saleRepository.getReturningCustomersBetweenDates(startDate, endDate);
            
            if (totalCustomers == 0) {
                return 0.0;
            }
            
            return (double) returningCustomers / totalCustomers * 100;
        } catch (Exception e) {
            log.error("Error calculating customer retention rate", e);
            throw new AnalyticsException("Failed to calculate customer retention rate", e);
        }
    }

    @Override
    public BigDecimal getCustomerAcquisitionCost(LocalDate startDate, LocalDate endDate) {
        try {
            BigDecimal totalMarketingCost = saleRepository.getTotalMarketingCostBetweenDates(startDate, endDate);
            int newCustomers = saleRepository.getNewCustomersBetweenDates(startDate, endDate);
            
            if (newCustomers == 0) {
                return BigDecimal.ZERO;
            }
            
            return totalMarketingCost.divide(BigDecimal.valueOf(newCustomers), 2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("Error calculating customer acquisition cost", e);
            throw new AnalyticsException("Failed to calculate customer acquisition cost", e);
        }
    }

    @Override
    public BigDecimal getCustomerLifetimeValue(LocalDate startDate, LocalDate endDate) {
        try {
            BigDecimal totalRevenue = getTotalRevenue(startDate, endDate);
            int totalCustomers = saleRepository.getTotalCustomersBetweenDates(startDate, endDate);
            
            if (totalCustomers == 0) {
                return BigDecimal.ZERO;
            }
            
            return totalRevenue.divide(BigDecimal.valueOf(totalCustomers), 2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("Error calculating customer lifetime value", e);
            throw new AnalyticsException("Failed to calculate customer lifetime value", e);
        }
    }

    private void updateConversionRate(ProductAnalytics analytics) {
        if (analytics.getViewCount() > 0) {
            double conversionRate = (double) analytics.getTotalSales() / analytics.getViewCount() * 100;
            analytics.setPurchaseConversionRate(Math.round(conversionRate * 100.0) / 100.0);
        }
    }
} 