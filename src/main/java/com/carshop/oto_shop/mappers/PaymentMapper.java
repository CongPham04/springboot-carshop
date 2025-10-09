package com.carshop.oto_shop.mappers;

import com.carshop.oto_shop.dto.payment.PaymentRequest;
import com.carshop.oto_shop.dto.payment.PaymentResponse;
import com.carshop.oto_shop.entities.Payment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Payment toPayment(PaymentRequest paymentRequest);

    @Mapping(source = "order.orderId", target = "orderId")
    PaymentResponse toPaymentResponse(Payment payment);
}
