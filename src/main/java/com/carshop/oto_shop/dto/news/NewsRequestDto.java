package com.carshop.oto_shop.dto.news;

import com.carshop.oto_shop.enums.NewsStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

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

    private NewsStatus status = NewsStatus.DRAFT;

    private MultipartFile coverImageFile; // <--- thay cho coverImageUrl

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getExcerpt() { return excerpt; }
    public void setExcerpt(String excerpt) { this.excerpt = excerpt; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public NewsStatus getStatus() { return status; }
    public void setStatus(NewsStatus status) { this.status = status; }

    public MultipartFile getCoverImageFile() { return coverImageFile; }
    public void setCoverImageFile(MultipartFile coverImageFile) { this.coverImageFile = coverImageFile; }
}
