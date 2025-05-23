package org.aldousdev.analyticsservice.repository;

import org.aldousdev.analyticsservice.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface SaleRepository extends JpaRepository<Sale, UUID> {
    List<Sale> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);
    List<Sale> findByUserIdAndSaleDate(UUID userId, LocalDate saleDate);

    @Query("SELECT SUM(s.totalRevenue) FROM Sale s WHERE s.saleDate >= :startDate AND s.saleDate <= :endDate")
    BigDecimal getTotalRevenueBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(s.profit) FROM Sale s WHERE s.saleDate >= :startDate AND s.saleDate <= :endDate")
    BigDecimal getTotalProfitBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.saleDate >= :startDate AND s.saleDate <= :endDate")
    int getTotalSalesBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(s.quantity) FROM Sale s WHERE s.productId = :productId AND s.saleDate >= :startDate AND s.saleDate <= :endDate")
    int getTotalSalesForProductBetweenDates(@Param("productId") UUID productId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(DISTINCT s.userId) FROM Sale s WHERE s.saleDate >= :startDate AND s.saleDate <= :endDate")
    int getTotalCustomersBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(DISTINCT s.userId) FROM Sale s WHERE s.saleDate >= :startDate AND s.saleDate <= :endDate AND s.userId IN (SELECT DISTINCT s2.userId FROM Sale s2 WHERE s2.saleDate < :startDate)")
    int getReturningCustomersBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(mc.cost), 0) FROM MarketingCost mc WHERE mc.date >= :startDate AND mc.date <= :endDate")
    BigDecimal getTotalMarketingCostBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(DISTINCT s.userId) FROM Sale s WHERE s.saleDate >= :startDate AND s.saleDate <= :endDate AND s.userId NOT IN (SELECT DISTINCT s2.userId FROM Sale s2 WHERE s2.saleDate < :startDate)")
    int getNewCustomersBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
