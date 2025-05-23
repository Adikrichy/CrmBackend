package org.aldousdev.analyticsservice.service.Impls;

import jakarta.persistence.EntityNotFoundException;
import org.aldousdev.analyticsservice.dto.SummaryResponseDto;
import org.aldousdev.analyticsservice.entity.DailySummary;
import org.aldousdev.analyticsservice.entity.Sale;
import org.aldousdev.analyticsservice.repository.DailySummaryRepository;
import org.aldousdev.analyticsservice.repository.SaleRepository;
import org.aldousdev.analyticsservice.service.interfaces.SummaryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SummaryServiceImpl implements SummaryService {

     private final DailySummaryRepository dailySummaryRepository;
     private final SaleRepository saleRepository;
     public SummaryServiceImpl(DailySummaryRepository dailySummaryRepository, SaleRepository saleRepository) {
         this.dailySummaryRepository = dailySummaryRepository;
         this.saleRepository = saleRepository;
     }

     @Override
     public DailySummary generateDailySummary(UUID userId, LocalDate date){
          List<Sale> sales = saleRepository.findByUserIdAndSaleDate(userId, date);
          int totalSalesCount = sales.size();
          int totalProductsSold = sales.stream()
                  .mapToInt(Sale -> Sale.getQuantity())
                  .sum();
          double totalRevenue = sales.stream()
                  .mapToDouble(Sale ->Sale.getTotalRevenue())
                  .sum();
          double totalCost = sales.stream()
                  .mapToDouble(Sale -> Sale.getTotalCost())
                  .sum();
          double totalProfit = sales.stream()
                  .mapToDouble(Sale -> Sale.getProfit())
                  .sum();
          DailySummary dailySummary = DailySummary.builder()
                  .userId(userId)
                  .date(date.atStartOfDay())
                  .totalProductsSold(totalProductsSold)
                  .totalRevenue(BigDecimal.valueOf(totalRevenue))
                  .totalCost(BigDecimal.valueOf(totalCost))
                  .totalSalesCount(totalSalesCount)
                  .totalProfit(BigDecimal.valueOf(totalProfit))
                  .build();

          DailySummary savedDailySummary = dailySummaryRepository.save(dailySummary);
          return savedDailySummary;

     }
    @Override
    public DailySummary getDailySummary(UUID userId, LocalDate date) {
        return dailySummaryRepository.findByUserIdAndDate(userId, date)
                .orElse(null);
    }

    @Override
     public List<DailySummary> getSummaryBetween(UUID userId, LocalDate start, LocalDate end) {
         return dailySummaryRepository.findByUserIdAndDateBetween(
                 userId,
                 start,
                 end
         );
     }

}
