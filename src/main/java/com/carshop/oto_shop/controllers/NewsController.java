package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.news.NewsRequestDto;
import com.carshop.oto_shop.dto.news.NewsResponseDto;
import com.carshop.oto_shop.services.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/news")
@Tag(name = "NewsController")
public class NewsController {

    private final NewsService newsService;
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // CREATE
    @Operation(summary = "Create news", description = "Thêm bài viết mới (có ảnh upload)")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<NewsResponseDto>> createNews(@Valid @ModelAttribute NewsRequestDto dto) {
        NewsResponseDto created = newsService.createNews(dto);
        return ResponseEntity.ok(ApiResponse.success("Tạo bài viết thành công!", created));
    }

    // UPDATE
    @Operation(summary = "Update news", description = "Cập nhật bài viết (có thể kèm ảnh mới)")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<NewsResponseDto>> updateNews(
            @PathVariable Long id,
            @Valid @ModelAttribute NewsRequestDto dto) {
        NewsResponseDto updated = newsService.updateNews(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật bài viết thành công!", updated));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok(ApiResponse.success("Xoá bài viết thành công!"));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<ApiResponse<List<NewsResponseDto>>> getAllNews() {
        List<NewsResponseDto> list = newsService.getAllNews();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách bài viết thành công!", list));
    }

    // GET DETAIL
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsResponseDto>> getNewsById(@PathVariable Long id) {
        NewsResponseDto dto = newsService.getNewsById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy bài viết thành công!", dto));
    }

    @Operation(summary = "Get image", description = "API lấy hình ảnh của bài viết tin tức")
    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            // Thư mục chứa ảnh tin tức
            Path filePath = Paths.get(NewsService.UPLOAD_DIR).resolve(filename).normalize();

            // Kiểm tra file tồn tại
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new AppException(ErrorCode.FILE_NOT_FOUND);
            }

            // Xác định loại MIME của file
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // Trả file về cho client
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    // Bỏ cache để tránh lỗi 304 Not Modified
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                    .header(HttpHeaders.PRAGMA, "no-cache")
                    .header(HttpHeaders.EXPIRES, "0")
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }
}
