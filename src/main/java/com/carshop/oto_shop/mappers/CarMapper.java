package com.carshop.oto_shop.mappers;

import com.carshop.oto_shop.dto.car.CarRequest;
import com.carshop.oto_shop.entities.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {
    @Mapping(target = "carId", ignore = true)
    @Mapping(target = "carCategory", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    Car toCar(CarRequest carRequest);
}
