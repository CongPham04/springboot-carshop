package com.carshop.oto_shop.repositories;

import com.carshop.oto_shop.entities.Order;
import com.carshop.oto_shop.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser_UserId(String userId);
    List<Order> findByStatus(OrderStatus status);
}
