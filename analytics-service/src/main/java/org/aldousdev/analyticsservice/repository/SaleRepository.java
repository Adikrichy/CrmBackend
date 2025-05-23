package org.aldousdev.analyticsservice.repository;

import org.aldousdev.analyticsservice.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SaleRepository extends JpaRepository<Sale, UUID> {
    List<Sale> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);
    List<Sale> findByUserIdAndSaleDate(UUID userId, LocalDate saleDate);


}
