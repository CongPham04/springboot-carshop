package com.carshop.oto_shop.mappers;

import com.carshop.oto_shop.dto.carbrand.CarBrandRequest;
import com.carshop.oto_shop.dto.carbrand.CarBrandResponse;
import com.carshop.oto_shop.entities.CarBrand;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CarBrandMapper {
    CarBrand toCarBrand(CarBrandRequest carBrandRequest);
    CarBrandResponse toCarBrandResponse(CarBrand carBrand);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCarBrandFromRequest(CarBrandRequest carBrandRequest, @MappingTarget CarBrand carBrand);

}
