package com.carshop.oto_shop.controllers;

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
    public ResponseEntity<List<PromotionResponseDto>> getActivePromotions() {
        // For now, returning all. Logic to filter by active and date can be added.
        return ResponseEntity.ok(promotionService.getAllPromotions());
    }

    // Admin endpoints
    @PostMapping
    public ResponseEntity<PromotionResponseDto> createPromotion(@Valid @RequestBody PromotionRequestDto requestDto) {
        return ResponseEntity.ok(promotionService.createPromotion(requestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromotionResponseDto> updatePromotion(@PathVariable Long id, @Valid @RequestBody PromotionRequestDto requestDto) {
        return ResponseEntity.ok(promotionService.updatePromotion(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }
}
