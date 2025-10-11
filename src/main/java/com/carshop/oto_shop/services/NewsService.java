package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.BadRequestException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.news.NewsRequestDto;
import com.carshop.oto_shop.dto.news.NewsResponseDto;
import com.carshop.oto_shop.entities.News;
import com.carshop.oto_shop.mappers.NewsMapper;
import com.carshop.oto_shop.repositories.NewsRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class NewsService {
    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    public static final String UPLOAD_DIR = "uploads/news/";
    private static final String BASE_IMAGE_URL = "http://localhost:8080/carshop/api/news/image/";

    public NewsService(NewsRepository newsRepository, NewsMapper newsMapper) {
        this.newsRepository = newsRepository;
        this.newsMapper = newsMapper;
    }

    public NewsResponseDto createNews(NewsRequestDto requestDto) {
        try {
            News news = newsMapper.toNews(requestDto);
            if (requestDto.getCoverImageFile() != null && !requestDto.getCoverImageFile().isEmpty()) {
                String imageUrl = saveImage(requestDto.getCoverImageFile());
                news.setCoverImageUrl(imageUrl);
            }
            newsRepository.save(news);
            return newsMapper.toNewsResponseDto(news);
        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException("Thiếu thông tin bắt buộc");
        }
    }

    @Transactional
    public NewsResponseDto updateNews(Long id, NewsRequestDto requestDto) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));

        newsMapper.updateNewsFromDto(requestDto, news);

        if (requestDto.getCoverImageFile() != null && !requestDto.getCoverImageFile().isEmpty()) {
            deleteImageFile(news.getCoverImageUrl());
            String newUrl = saveImage(requestDto.getCoverImageFile());
            news.setCoverImageUrl(newUrl);
        }

        newsRepository.save(news);
        return newsMapper.toNewsResponseDto(news);
    }

    public List<NewsResponseDto> getAllNews() {
        List<News> list = newsRepository.findAll();
        return list.stream()
                .map(news -> {
                    NewsResponseDto dto = newsMapper.toNewsResponseDto(news);
                    if (news.getCoverImageUrl() != null) {
                        String fileName = Paths.get(news.getCoverImageUrl()).getFileName().toString();
                        dto.setCoverImageUrl(BASE_IMAGE_URL + fileName);
                    }
                    return dto;
                })
                .toList();
    }

    public NewsResponseDto getNewsById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));
        NewsResponseDto dto = newsMapper.toNewsResponseDto(news);
        if (news.getCoverImageUrl() != null) {
            String fileName = Paths.get(news.getCoverImageUrl()).getFileName().toString();
            dto.setCoverImageUrl(BASE_IMAGE_URL + fileName);
        }
        return dto;
    }

    @Transactional
    public void deleteNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));
        deleteImageFile(news.getCoverImageUrl());
        newsRepository.delete(news);
    }

    private String saveImage(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.startsWith("image/"))) {
                throw new AppException(ErrorCode.UNSUPPORTED_MEDIA_TYPE);
            }

            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return UPLOAD_DIR + fileName;
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    private void deleteImageFile(String imageUrl) {
        if (imageUrl == null) return;
        try {
            String fileName = Paths.get(imageUrl).getFileName().toString();
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
            File file = filePath.toFile();
            if (file.exists()) file.delete();
        } catch (Exception e) {
            logger.error("Không thể xoá ảnh: " + e.getMessage());
        }
    }
}
