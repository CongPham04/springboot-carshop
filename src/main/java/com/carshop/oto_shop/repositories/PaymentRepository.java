package com.carshop.oto_shop.repositories;

import com.carshop.oto_shop.entities.Payment;
import com.carshop.oto_shop.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByOrder_OrderId(String orderId);
    List<Payment> findByStatus(PaymentStatus status);
}
