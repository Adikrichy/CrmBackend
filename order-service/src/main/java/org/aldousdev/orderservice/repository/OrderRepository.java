package org.aldousdev.orderservice.repository;

import org.aldousdev.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(String userId);
    List<Order> findByStatus(Order.OrderStatus status);
    List<Order> findByUserIdAndStatus(String userId, Order.OrderStatus status);
} 