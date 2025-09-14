package com.carshop.oto_shop.mappers;

import com.carshop.oto_shop.dto.carcategory.CarCategoryRequest;
import com.carshop.oto_shop.dto.carcategory.CarCategoryResponse;
import com.carshop.oto_shop.entities.CarCategory;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper (componentModel = "spring")
public interface CarCategoryMapper {
    CarCategory toCarCategory(CarCategoryRequest carCategoryRequest);
    CarCategoryResponse toCarCategoryResponse(CarCategory carCategory);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCarCategoryRequest(CarCategoryRequest carCategoryRequest, @MappingTarget CarCategory carCategory);
}
