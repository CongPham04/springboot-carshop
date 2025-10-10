package com.carshop.oto_shop.dto.promotion;

import com.carshop.oto_shop.enums.DiscountType;
import com.carshop.oto_shop.enums.PromotionScope;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PromotionRequestDto {
    @NotEmpty
    private String title;
    private String description;
    @NotNull
    private DiscountType discountType;
    @NotNull
    private BigDecimal discountValue;
    @NotNull
    @FutureOrPresent
    private LocalDateTime startAt;
    @NotNull
    @Future
    private LocalDateTime endAt;
    private boolean active = true;
    @NotNull
    private PromotionScope appliesTo;
    private List<String> targetCategories;
    private List<String> targetBrands;
    private List<Long> targetCarIds;
}
