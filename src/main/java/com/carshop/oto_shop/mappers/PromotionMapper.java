package com.carshop.oto_shop.mappers;

import com.carshop.oto_shop.dto.promotion.PromotionRequestDto;
import com.carshop.oto_shop.dto.promotion.PromotionResponseDto;
import com.carshop.oto_shop.entities.Promotion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    @Mapping(target = "appliesTo", source = "appliesTo")
    @Mapping(target = "targetCategories", expression = "java(toJson(dto.getTargetCategories()))")
    @Mapping(target = "targetBrands", expression = "java(toJson(dto.getTargetBrands()))")
    @Mapping(target = "targetCars", ignore = true) // sẽ xử lý riêng ở service nếu cần
    Promotion toPromotion(PromotionRequestDto dto);

    @Mapping(target = "targetCategories", expression = "java(fromJson(promotion.getTargetCategories()))")
    @Mapping(target = "targetBrands", expression = "java(fromJson(promotion.getTargetBrands()))")
    PromotionResponseDto toPromotionResponseDto(Promotion promotion);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "targetCategories", expression = "java(toJson(dto.getTargetCategories()))")
    @Mapping(target = "targetBrands", expression = "java(toJson(dto.getTargetBrands()))")
    @Mapping(target = "targetCars", ignore = true)
    void updatePromotionFromDto(PromotionRequestDto dto, @MappingTarget Promotion promotion);

    // Dùng Jackson để convert sang JSON string
    default String toJson(Object obj) {
        try {
            if (obj == null) return null;
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON mapping error", e);
        }
    }
    // Convert JSON -> List
    default List<String> fromJson(String json) {
        try {
            if (json == null) return null;
            return new ObjectMapper().readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("JSON mapping error (fromJson)", e);
        }
    }
}
