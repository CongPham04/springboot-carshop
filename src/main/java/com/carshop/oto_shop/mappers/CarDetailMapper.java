package com.carshop.oto_shop.mappers;

import com.carshop.oto_shop.dto.cardetail.CarDetailRequest;
import com.carshop.oto_shop.dto.cardetail.CarDetailResponse;
import com.carshop.oto_shop.entities.CarDetail;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CarDetailMapper {
    CarDetail toCarDetail(CarDetailRequest carDetailRequest);
    CarDetailResponse toCarDetailResponse(CarDetail carDetail);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCarDetail(CarDetailRequest request, @MappingTarget CarDetail carDetail);
}
