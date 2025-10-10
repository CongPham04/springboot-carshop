package com.carshop.oto_shop.entities;

import com.carshop.oto_shop.enums.DiscountType;
import com.carshop.oto_shop.enums.PromotionScope;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_id")
    private Long promotionId;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @Column(name = "discount_value", nullable = false, precision = 12, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "applies_to", nullable = false)
    private PromotionScope appliesTo;

    @Column(name = "target_categories", columnDefinition = "JSON")
    private String targetCategories; // Storing as JSON string

    @Column(name = "target_brands", columnDefinition = "JSON")
    private String targetBrands; // Storing as JSON string

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "promotion_cars",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "car_id")
    )
    private Set<Car> targetCars = new HashSet<>();

    // Getters and Setters

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

    public String getTargetCategories() {
        return targetCategories;
    }

    public void setTargetCategories(String targetCategories) {
        this.targetCategories = targetCategories;
    }

    public String getTargetBrands() {
        return targetBrands;
    }

    public void setTargetBrands(String targetBrands) {
        this.targetBrands = targetBrands;
    }

    public Set<Car> getTargetCars() {
        return targetCars;
    }

    public void setTargetCars(Set<Car> targetCars) {
        this.targetCars = targetCars;
    }
}
