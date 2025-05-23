package org.aldousdev.analyticsservice.service.interfaces;

import org.aldousdev.analyticsservice.dto.ProductAnalyticsDTO;
import org.aldousdev.analyticsservice.entity.Sale;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for handling analytics operations
 */
public interface AnalyticsService {
    
    /**
     * Updates product analytics based on a new sale
     * @param sale The sale information to process
     */
    void updateProductAnalytics(Sale sale);

    /**
     * Retrieves analytics for a specific product
     * @param productId The ID of the product
     * @return Product analytics data
     */
    ProductAnalyticsDTO getProductAnalytics(UUID productId);

    /**
     * Gets the top selling products
     * @return List of top selling products with their analytics
     */
    List<ProductAnalyticsDTO> getTopSellingProducts();

    /**
     * Gets products with the best conversion rates
     * @return List of products with best conversion rates
     */
    List<ProductAnalyticsDTO> getBestConvertingProducts();

    /**
     * Gets products with low stock levels
     * @param threshold The stock level threshold
     * @return List of products with stock below threshold
     */
    List<ProductAnalyticsDTO> getLowStockProducts(int threshold);

    /**
     * Gets the most viewed products
     * @return List of most viewed products
     */
    List<ProductAnalyticsDTO> getMostViewedProducts();

    /**
     * Updates the view count for a product
     * @param productId The ID of the product
     */
    void updateProductView(UUID productId);

    /**
     * Updates the cart addition count for a product
     * @param productId The ID of the product
     */
    void updateCartAddition(UUID productId);

    /**
     * Calculates total revenue for a date range
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Total revenue for the period
     */
    BigDecimal getTotalRevenue(LocalDate startDate, LocalDate endDate);

    /**
     * Calculates total profit for a date range
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Total profit for the period
     */
    BigDecimal getTotalProfit(LocalDate startDate, LocalDate endDate);

    /**
     * Gets total number of sales for a date range
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Total number of sales
     */
    int getTotalSales(LocalDate startDate, LocalDate endDate);

    /**
     * Calculates average order value for a date range
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Average order value
     */
    BigDecimal getAverageOrderValue(LocalDate startDate, LocalDate endDate);

    /**
     * Gets trending products based on views, cart additions, and sales
     * @param limit Maximum number of products to return
     * @return List of trending products
     */
    List<ProductAnalyticsDTO> getTrendingProducts(int limit);

    /**
     * Gets products by category
     * @param category The category to filter by
     * @return List of products in the category
     */
    List<ProductAnalyticsDTO> getProductsByCategory(String category);

    /**
     * Gets products with highest profit margin
     * @param limit Maximum number of products to return
     * @return List of products with highest profit margins
     */
    List<ProductAnalyticsDTO> getHighestProfitMarginProducts(int limit);

    /**
     * Gets products with highest customer satisfaction
     * @param limit Maximum number of products to return
     * @return List of products with highest ratings
     */
    List<ProductAnalyticsDTO> getHighestRatedProducts(int limit);

    /**
     * Gets products that need restocking based on sales velocity
     * @param daysToStockout Number of days until stockout
     * @return List of products that need restocking
     */
    List<ProductAnalyticsDTO> getProductsNeedingRestock(int daysToStockout);

    /**
     * Gets sales velocity (units sold per day) for a product
     * @param productId The ID of the product
     * @param days Number of days to analyze
     * @return Sales velocity
     */
    double getSalesVelocity(UUID productId, int days);

    /**
     * Gets customer retention rate for a period
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Customer retention rate as a percentage
     */
    double getCustomerRetentionRate(LocalDate startDate, LocalDate endDate);

    /**
     * Gets customer acquisition cost for a period
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Customer acquisition cost
     */
    BigDecimal getCustomerAcquisitionCost(LocalDate startDate, LocalDate endDate);

    /**
     * Gets customer lifetime value
     * @param startDate Start date of the period
     * @param endDate End date of the period
     * @return Customer lifetime value
     */
    BigDecimal getCustomerLifetimeValue(LocalDate startDate, LocalDate endDate);
}
