package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.news.NewsRequestDto;
import com.carshop.oto_shop.dto.news.NewsResponseDto;
import com.carshop.oto_shop.entities.News;
import com.carshop.oto_shop.mappers.NewsMapper;
import com.carshop.oto_shop.repositories.NewsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    public NewsService(NewsRepository newsRepository, NewsMapper newsMapper) {
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
    }

    @Transactional
    public NewsResponseDto createNews(NewsRequestDto requestDto) {
        News news = newsMapper.toNews(requestDto);
        News savedNews = newsRepository.save(news);
        return newsMapper.toNewsResponseDto(savedNews);
    }

    @Transactional(readOnly = true)
    public NewsResponseDto getNewsById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));
        return newsMapper.toNewsResponseDto(news);
    }

    @Transactional(readOnly = true)
    public List<NewsResponseDto> getAllNews() {
        return newsRepository.findAll().stream()
                .map(newsMapper::toNewsResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public NewsResponseDto updateNews(Long id, NewsRequestDto requestDto) {
        News existingNews = newsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));

        newsMapper.updateNewsFromDto(requestDto, existingNews);
        News updatedNews = newsRepository.save(existingNews);
        return newsMapper.toNewsResponseDto(updatedNews);
    }

    @Transactional
    public void deleteNews(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new AppException(ErrorCode.NEWS_NOT_FOUND);
        }
        newsRepository.deleteById(id);
    }
}
