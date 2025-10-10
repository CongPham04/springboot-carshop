package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.promotion.PromotionRequestDto;
import com.carshop.oto_shop.dto.promotion.PromotionResponseDto;
import com.carshop.oto_shop.entities.Promotion;
import com.carshop.oto_shop.mappers.PromotionMapper;
import com.carshop.oto_shop.repositories.PromotionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    public PromotionService(PromotionRepository promotionRepository, PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    @Transactional
    public PromotionResponseDto createPromotion(PromotionRequestDto requestDto) {
        Promotion promotion = promotionMapper.toPromotion(requestDto);
        Promotion savedPromotion = promotionRepository.save(promotion);
        return promotionMapper.toPromotionResponseDto(savedPromotion);
    }

    @Transactional(readOnly = true)
    public PromotionResponseDto getPromotionById(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));
        return promotionMapper.toPromotionResponseDto(promotion);
    }

    @Transactional(readOnly = true)
    public List<PromotionResponseDto> getAllPromotions() {
        return promotionRepository.findAll().stream()
                .map(promotionMapper::toPromotionResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PromotionResponseDto updatePromotion(Long id, PromotionRequestDto requestDto) {
        Promotion existingPromotion = promotionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROMOTION_NOT_FOUND));

        promotionMapper.updatePromotionFromDto(requestDto, existingPromotion);
        Promotion updatedPromotion = promotionRepository.save(existingPromotion);
        return promotionMapper.toPromotionResponseDto(updatedPromotion);
    }

    @Transactional
    public void deletePromotion(Long id) {
        if (!promotionRepository.existsById(id)) {
            throw new AppException(ErrorCode.PROMOTION_NOT_FOUND);
        }
        promotionRepository.deleteById(id);
    }
}
