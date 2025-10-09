package com.carshop.oto_shop.repositories;

import com.carshop.oto_shop.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface
OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrder_OrderId(String orderId);
}
