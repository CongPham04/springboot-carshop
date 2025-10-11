package com.carshop.oto_shop.mappers;

import com.carshop.oto_shop.dto.news.NewsRequestDto;
import com.carshop.oto_shop.dto.news.NewsResponseDto;
import com.carshop.oto_shop.entities.News;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    // Tạo News từ NewsRequestDto (bỏ qua ảnh)
    @Mapping(target = "newsId", ignore = true)
    @Mapping(target = "coverImageUrl", ignore = true) // Ảnh xử lý trong service
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    News toNews(NewsRequestDto dto);

    // Tạo NewsResponseDto từ News entity
    @Mapping(target = "coverImageUrl", ignore = false) // Giữ lại URL đầy đủ (service set sẵn)
    NewsResponseDto toNewsResponseDto(News news);

    // Cập nhật News từ DTO, bỏ qua ảnh và id
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "newsId", ignore = true)
    @Mapping(target = "coverImageUrl", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateNewsFromDto(NewsRequestDto dto, @MappingTarget News news);
}
