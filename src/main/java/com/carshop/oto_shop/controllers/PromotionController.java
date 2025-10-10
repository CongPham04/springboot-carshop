package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.promotion.PromotionRequestDto;
import com.carshop.oto_shop.dto.promotion.PromotionResponseDto;
import com.carshop.oto_shop.services.PromotionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    // Public endpoints
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<PromotionResponseDto>>> getActivePromotions() {
        // For now, returning all. Logic to filter by active and date can be added.
        List<PromotionResponseDto> responseDtos = promotionService.getAllPromotions();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách khuyến mãi thành công", responseDtos));
    }

    // Admin endpoints
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createPromotion(@Valid @RequestBody PromotionRequestDto requestDto) {
        promotionService.createPromotion(requestDto);
        return ResponseEntity.ok(ApiResponse.success("Tạo khuyến mãi thành công!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updatePromotion(@PathVariable Long id, @Valid @RequestBody PromotionRequestDto requestDto) {
        promotionService.updatePromotion(id, requestDto);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật khuyến mãi thành công!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.ok(ApiResponse.success("Xoá khuyến mãi thành công"));
    }
}
