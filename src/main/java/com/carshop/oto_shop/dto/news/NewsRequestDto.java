package com.carshop.oto_shop.dto.news;

import com.carshop.oto_shop.enums.NewsStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewsRequestDto {
    @NotEmpty
    @Size(max = 180)
    private String title;

    @NotEmpty
    @Size(max = 200)
    private String slug;

    @Size(max = 300)
    private String excerpt;

    @NotEmpty
    private String content;

    private String coverImageUrl;

    private NewsStatus status = NewsStatus.DRAFT;
}
