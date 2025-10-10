package com.carshop.oto_shop.dto.news;

import com.carshop.oto_shop.enums.NewsStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsResponseDto {
    private Long newsId;
    private String title;
    private String slug;
    private String excerpt;
    private String content;
    private String coverImageUrl;
    private NewsStatus status;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
