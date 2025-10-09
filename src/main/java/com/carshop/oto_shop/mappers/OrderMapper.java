package com.carshop.oto_shop.mappers;

import com.carshop.oto_shop.dto.order.OrderRequest;
import com.carshop.oto_shop.dto.order.OrderResponse;
import com.carshop.oto_shop.entities.Order;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {OrderDetailMapper.class, PaymentMapper.class})
public interface OrderMapper {

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "payment", ignore = true)
    Order toOrder(OrderRequest orderRequest);

    @Mapping(source = "user.userId", target = "userId")
    OrderResponse toOrderResponse(Order order);
}
