package com.carshop.oto_shop.mappers;

import com.carshop.oto_shop.dto.car.CarRequest;
import com.carshop.oto_shop.dto.car.CarResponse;
import com.carshop.oto_shop.entities.Car;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CarMapper {
    @Mapping(target = "carId", ignore = true)
    @Mapping(target = "carCategory", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    Car toCar(CarRequest carRequest);
    @Mapping(target = "imageUrl", ignore = false)
    CarResponse toCarResponse(Car car);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "imageUrl", ignore = true)    // không overwrite imageUrl
    void updateCarRequest(CarRequest carRequest, @MappingTarget Car car);

}
