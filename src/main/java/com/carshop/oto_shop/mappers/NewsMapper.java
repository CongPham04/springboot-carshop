package com.carshop.oto_shop.mappers;

import com.carshop.oto_shop.dto.news.NewsRequestDto;
import com.carshop.oto_shop.dto.news.NewsResponseDto;
import com.carshop.oto_shop.entities.News;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    News toNews(NewsRequestDto dto);
    NewsResponseDto toNewsResponseDto(News news);
    void updateNewsFromDto(NewsRequestDto dto, @MappingTarget News news);
}
