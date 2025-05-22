package org.aldousdev.analyticsservice.repository;

import org.aldousdev.analyticsservice.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SaleRepository extends JpaRepository<Sale, UUID> {
    List<Sale> findByUserId(UUID userId);
}
