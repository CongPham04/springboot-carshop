package com.carshop.oto_shop.dto.promotion;

import com.carshop.oto_shop.enums.DiscountType;
import com.carshop.oto_shop.enums.PromotionScope;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
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
}
