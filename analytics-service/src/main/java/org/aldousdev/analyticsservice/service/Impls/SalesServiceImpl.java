package org.aldousdev.analyticsservice.service.Impls;

import org.aldousdev.analyticsservice.dto.OrderCreatedEvent;
import org.aldousdev.analyticsservice.entity.Sale;
import org.aldousdev.analyticsservice.repository.SaleRepository;
import org.aldousdev.analyticsservice.service.interfaces.SalesService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SalesServiceImpl implements SalesService {
    private final SaleRepository saleRepository;

    public SalesServiceImpl(SaleRepository saleRepository){
        this.saleRepository = saleRepository;
    }

    @Override
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        List<Sale> sales = mapOrderSales(event);
        saleRepository.saveAll(sales);
    }

    @Override
    public List<Sale> getSalesByUserId(UUID userId) {
        return saleRepository.findByUserId(userId);
    }

    @Override
    public List<Sale> getSalesByUserIdAndDate(UUID userId, LocalDate date) {
        return saleRepository.findByUserIdAndSaleDate(userId, date);
    }
    @Override
    public void deleteSalesByUserId(UUID userId) {
        saleRepository.deleteByUserId(userId);
    }

    public List<Sale> mapOrderSales(OrderCreatedEvent event){
        return event.getItems().stream()
                .map(item ->{
                    BigDecimal totalRevenue = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
                    BigDecimal totalCost = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
                    BigDecimal profit = totalCost.subtract(totalRevenue);

                    return Sale.builder()
                            .userId(event.getUserId())
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .salesPrice(item.getUnitPrice())
                            .costPrice(item.getCostPrice())
                            .totalRevenue(totalRevenue.doubleValue())
                            .totalCost(totalCost.doubleValue())
                            .profit(profit.doubleValue())
                            .orderId(event.getOrderId())
                            .saleDate(event.getCreatedAt().toLocalDate())
                            .build();

                })
                .toList();
    }
}
