package org.aldousdev.analyticsservice.repository;

import org.aldousdev.analyticsservice.entity.ProductAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductAnalyticsRepository extends JpaRepository<ProductAnalytics, UUID> {
    
    ProductAnalytics findByProductId(UUID productId);
    
    @Query("SELECT p FROM ProductAnalytics p ORDER BY p.totalRevenue DESC")
    List<ProductAnalytics> findTopSellingProducts();
    
    @Query("SELECT p FROM ProductAnalytics p ORDER BY p.purchaseConversionRate DESC")
    List<ProductAnalytics> findBestConvertingProducts();
    
    @Query("SELECT p FROM ProductAnalytics p WHERE p.stockLevel < :threshold")
    List<ProductAnalytics> findLowStockProducts(@Param("threshold") int threshold);
    
    @Query("SELECT p FROM ProductAnalytics p ORDER BY p.viewCount DESC")
    List<ProductAnalytics> findMostViewedProducts();
    
    @Query("SELECT p FROM ProductAnalytics p WHERE p.category = :category")
    List<ProductAnalytics> findByCategory(@Param("category") String category);
    
    @Query("SELECT p FROM ProductAnalytics p ORDER BY (p.viewCount * 0.4 + p.cartAdditions * 0.3 + p.totalSales * 0.3) DESC")
    List<ProductAnalytics> findTrendingProducts(@Param("limit") int limit);
    
    @Query("SELECT p FROM ProductAnalytics p WHERE p.lastUpdated >= :startDate AND p.lastUpdated <= :endDate")
    List<ProductAnalytics> findByDateRange(@Param("startDate") java.time.LocalDateTime startDate, 
                                         @Param("endDate") java.time.LocalDateTime endDate);
    
    @Query("SELECT p FROM ProductAnalytics p ORDER BY (p.totalProfit / NULLIF(p.totalRevenue, 0)) DESC")
    List<ProductAnalytics> findHighestProfitMarginProducts(@Param("limit") int limit);
    
    @Query("SELECT p FROM ProductAnalytics p ORDER BY p.averageRating DESC")
    List<ProductAnalytics> findHighestRatedProducts(@Param("limit") int limit);
    
    @Query("SELECT p FROM ProductAnalytics p WHERE p.stockLevel <= (p.totalSales / :daysToStockout)")
    List<ProductAnalytics> findProductsNeedingRestock(@Param("daysToStockout") int daysToStockout);
    
    @Query("SELECT p FROM ProductAnalytics p WHERE p.totalSales > 0 ORDER BY (p.totalSales / DATEDIFF(day, p.firstSaleDate, CURRENT_DATE)) DESC")
    List<ProductAnalytics> findFastestSellingProducts(@Param("limit") int limit);
    
    @Query("SELECT p FROM ProductAnalytics p WHERE p.cartAdditions > 0 ORDER BY (p.totalSales / p.cartAdditions) DESC")
    List<ProductAnalytics> findBestConvertingProductsByCart(@Param("limit") int limit);
    
    @Query("SELECT p FROM ProductAnalytics p WHERE p.viewCount > 0 ORDER BY (p.totalSales / p.viewCount) DESC")
    List<ProductAnalytics> findBestConvertingProductsByViews(@Param("limit") int limit);
} 