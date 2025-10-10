package com.carshop.oto_shop.dto.promotion;

import com.carshop.oto_shop.enums.DiscountType;
import com.carshop.oto_shop.enums.PromotionScope;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public class PromotionResponseDto {
    private Long promotionId;
    private String title;
    private String description;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private boolean active;
    private PromotionScope appliesTo;
    private List<String> targetCategories;
    private List<String> targetBrands;
    private List<Long> targetCarIds;

    public PromotionResponseDto() {
    }

    public PromotionResponseDto(Long promotionId, String title, String description, DiscountType discountType, BigDecimal discountValue, LocalDateTime startAt, LocalDateTime endAt, boolean active, PromotionScope appliesTo, List<String> targetCategories, List<String> targetBrands, List<Long> targetCarIds) {
        this.promotionId = promotionId;
        this.title = title;
        this.description = description;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.startAt = startAt;
        this.endAt = endAt;
        this.active = active;
        this.appliesTo = appliesTo;
        this.targetCategories = targetCategories;
        this.targetBrands = targetBrands;
        this.targetCarIds = targetCarIds;
    }

    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public PromotionScope getAppliesTo() {
        return appliesTo;
    }

    public void setAppliesTo(PromotionScope appliesTo) {
        this.appliesTo = appliesTo;
    }

    public List<String> getTargetCategories() {
        return targetCategories;
    }

    public void setTargetCategories(List<String> targetCategories) {
        this.targetCategories = targetCategories;
    }

    public List<String> getTargetBrands() {
        return targetBrands;
    }

    public void setTargetBrands(List<String> targetBrands) {
        this.targetBrands = targetBrands;
    }

    public List<Long> getTargetCarIds() {
        return targetCarIds;
    }

    public void setTargetCarIds(List<Long> targetCarIds) {
        this.targetCarIds = targetCarIds;
    }
}
