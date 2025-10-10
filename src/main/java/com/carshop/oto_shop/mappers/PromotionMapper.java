package com.carshop.oto_shop.mappers;

import com.carshop.oto_shop.dto.promotion.PromotionRequestDto;
import com.carshop.oto_shop.dto.promotion.PromotionResponseDto;
import com.carshop.oto_shop.entities.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    Promotion toPromotion(PromotionRequestDto dto);
    PromotionResponseDto toPromotionResponseDto(Promotion promotion);
    void updatePromotionFromDto(PromotionRequestDto dto, @MappingTarget Promotion promotion);
}
