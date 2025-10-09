package com.carshop.oto_shop.mappers;

import com.carshop.oto_shop.dto.orderdetail.OrderDetailRequest;
import com.carshop.oto_shop.dto.orderdetail.OrderDetailResponse;
import com.carshop.oto_shop.entities.OrderDetail;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    @Mapping(target = "orderDetailId", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "car", ignore = true)
    @Mapping(target = "price", ignore = true)
    OrderDetail toOrderDetail(OrderDetailRequest orderDetailRequest);

    @Mapping(source = "car.carId", target = "carId")
    @Mapping(source = "car.model", target = "carModel")
    @Mapping(target = "subtotal", expression = "java(orderDetail.getPrice().multiply(java.math.BigDecimal.valueOf(orderDetail.getQuantity())))")
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);
}
