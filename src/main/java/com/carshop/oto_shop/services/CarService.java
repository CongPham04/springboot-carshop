package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.BadRequestException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.car.CarRequest;
import com.carshop.oto_shop.dto.car.CarResponse;
import com.carshop.oto_shop.entities.Car;
import com.carshop.oto_shop.enums.Brand;
import com.carshop.oto_shop.enums.Category;
import com.carshop.oto_shop.mappers.CarMapper;
import com.carshop.oto_shop.repositories.CarDetailRepository;
import com.carshop.oto_shop.repositories.CarRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

// Import các thư viện cho Specification
import com.carshop.oto_shop.enums.Color;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;


@Service
public class CarService {
    private static final Logger logger = LoggerFactory.getLogger(CarService.class);
    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final CarDetailRepository carDetailRepository;
    public static final String UPLOAD_DIR = "uploads/cars/";
    private static final String BASE_IMAGE_URL = "http://localhost:8080/carshop/api/cars/image/";

    public CarService(CarRepository carRepository, CarMapper carMapper, CarDetailRepository carDetailRepository) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
        this.carDetailRepository = carDetailRepository;
    }

    // ... (Các phương thức createCar, updateCar, deleteCar, saveImage, deleteImageFile giữ nguyên) ...

    public void createCar(CarRequest carRequest) {
        try {
            Car car = carMapper.toCar(carRequest);
            String imageUrl = null;
            if (carRequest.getImageFile() != null && !carRequest.getImageFile().isEmpty()) {
                imageUrl = saveImage(carRequest.getImageFile());
            }
            logger.info("Creating car - Model: {}, Brand: {}, Category: {}",
                    carRequest.getModel(), carRequest.getBrand(), carRequest.getCategory());
            car.setImageUrl(imageUrl);
            carRepository.save(car);
        } catch (DataIntegrityViolationException ex) {
            String message = ex.getMostSpecificCause().getMessage();
            if (message != null && message.contains("cannot be null")) {
                String field = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));
                throw new BadRequestException(field + " không được để trống!");
            } else {
                throw new AppException(ErrorCode.UNKNOWN);
            }
        }
    }

    private void deleteImageFile(String imageUrl) {
        if (imageUrl == null)
            return;
        try {
            String fileName = Paths.get(imageUrl).getFileName().toString();
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
            File file = filePath.toFile();
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    logger.info("Da xoa file anh: " + filePath);
                } else {
                    logger.warn("Khong the xoa anh: " + filePath);
                }
            }
        } catch (Exception e) {
            logger.error("Loi khi xoa anh: " + e.getMessage());
        }
    }

    @Transactional
    public void updateCar(CarRequest carRequest, Long carId) {
        try {
            Car car = carRepository.findById(carId)
                    .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));
            carMapper.updateCarRequest(carRequest, car);
            if (carRequest.getImageFile() != null && !carRequest.getImageFile().isEmpty()) {
                // Xoá ảnh cũ trước
                deleteImageFile(car.getImageUrl());
                // Lưu ảnh mới
                String imageUrl = saveImage(carRequest.getImageFile());
                car.setImageUrl(imageUrl);
            }
            carRepository.save(car);
        } catch (DataIntegrityViolationException ex) {
            String message = ex.getMostSpecificCause().getMessage();
            if (message != null && message.contains("cannot be null")) {
                String field = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));
                throw new BadRequestException(field + " không được để trống!");
            } else {
                throw new AppException(ErrorCode.UNKNOWN);
            }
        }
    }

    private String saveImage(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            if (contentType == null ||
                    (!contentType.equals("image/png")
                            && !contentType.equals("image/jpeg")
                            && !contentType.equals("application/pdf"))) {
                throw new AppException(ErrorCode.UNSUPPORTED_MEDIA_TYPE);
            }
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return UPLOAD_DIR + fileName;
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    @Transactional
    public void deleteCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));
        deleteImageFile(car.getImageUrl());
        carDetailRepository.deleteAllByCarId(carId);
        carRepository.delete(car);
    }

    public CarResponse getCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));
        CarResponse response = carMapper.toCarResponse(car);
        if (car.getImageUrl() != null) {
            String fileName = Paths.get(car.getImageUrl()).getFileName().toString();
            response.setImageUrl(BASE_IMAGE_URL + fileName);
        }
        return response;
    }

    public List<CarResponse> getCarsByBrand(Brand brand) {
        List<Car> cars = carRepository.findAllByBrand(brand);
        return cars.stream()
                .map(car -> {
                    CarResponse response = carMapper.toCarResponse(car);
                    if (car.getImageUrl() != null) {
                        String fileName = Paths.get(car.getImageUrl()).getFileName().toString();
                        response.setImageUrl(BASE_IMAGE_URL + fileName);
                    }
                    return response;
                })
                .toList();
    }

    public List<CarResponse> getCarsByCategory(Category category) {
        List<Car> cars = carRepository.findAllByCategory(category);
        return cars.stream()
                .map(car -> {
                    CarResponse response = carMapper.toCarResponse(car);
                    if (car.getImageUrl() != null) {
                        String fileName = Paths.get(car.getImageUrl()).getFileName().toString();
                        response.setImageUrl(BASE_IMAGE_URL + fileName);
                    }
                    return response;
                })
                .toList();
    }

    // [PHƯƠNG THỨC ĐÃ SỬA LỖI]
    public List<CarResponse> searchCars(String keyword, String brand, String category,
                                        String color, Double priceMin, Double priceMax,
                                        Integer yearFrom, Integer yearTo) {

        Specification<Car> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // [SỬA LỖI KEYWORD]
            // Tìm kiếm theo (Tên xe, Hãng xe, Loại xe, VÀ Hãng + Tên)
            if (keyword != null && !keyword.isEmpty()) {
                // Thay thế nhiều khoảng trắng bằng 1 và chuyển sang chữ thường
                String keywordLower = "%" + keyword.toLowerCase().replaceAll("\\s+", " ") + "%";
                // Tạo một keyword '%mercedes%c200%' để tìm kiếm linh hoạt hơn
                String keywordSpaced = "%" + keyword.toLowerCase().replaceAll("\\s+", "%") + "%";

                // 1. Tìm theo Tên xe (model)
                Predicate modelLike = cb.like(cb.lower(root.get("model")), keywordSpaced);

                // 2. Tìm theo Hãng xe (brand)
                Predicate brandLike = cb.like(cb.lower(root.get("brand").as(String.class)), keywordSpaced);

                // 3. Tìm theo Loại xe (category)
                Predicate categoryLike = cb.like(cb.lower(root.get("category").as(String.class)), keywordSpaced);

                // 4. [LOGIC MỚI] Tìm theo Hãng + " " + Tên
                // (ví dụ: "MERCEDES C200")
                Predicate brandAndModelLike = cb.like(
                        cb.lower(
                                cb.concat(
                                        cb.concat(root.get("brand").as(String.class), " "), // Nối 'brand' + ' '
                                        root.get("model") // Nối 'model'
                                )
                        ),
                        keywordLower // Tìm kiếm chuỗi chính xác "mercedes c200"
                );

                // Kết hợp 4 điều kiện bằng 'OR'
                predicates.add(cb.or(modelLike, brandLike, categoryLike, brandAndModelLike));
            }
            // [HẾT SỬA LỖI KEYWORD]


            // Lọc theo Hãng xe (Brand)
            if (brand != null && !brand.isEmpty()) {
                try {
                    predicates.add(cb.equal(root.get("brand"), Brand.valueOf(brand.toUpperCase())));
                } catch (IllegalArgumentException e) {
                    logger.warn("Invalid brand value: {}", brand);
                }
            }

            // Lọc theo Loại xe (Category)
            if (category != null && !category.isEmpty()) {
                try {
                    predicates.add(cb.equal(root.get("category"), Category.valueOf(category.toUpperCase())));
                } catch (IllegalArgumentException e) {
                    logger.warn("Invalid category value: {}", category);
                }
            }

            // Lọc theo Màu sắc (Color)
            if (color != null && !color.isEmpty()) {
                try {
                    predicates.add(cb.equal(root.get("color"), Color.valueOf(color.toUpperCase())));
                } catch (IllegalArgumentException e) {
                    logger.warn("Invalid color value: {}", color);
                }
            }

            // Lọc theo Giá Tối thiểu (Price Min)
            if (priceMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), priceMin));
            }

            // Lọc theo Giá Tối đa (Price Max)
            if (priceMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), priceMax));
            }

            // Lọc chính xác theo năm
            if (yearFrom != null) {
                predicates.add(cb.equal(root.get("manufactureYear"), yearFrom));
            }

            // Lọc theo Năm sản xuất (Đến năm)
            if (yearTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("manufactureYear"), yearTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<Car> cars = carRepository.findAll(spec);

        // Ánh xạ kết quả sang CarResponse
        return cars.stream()
                .map(car -> {
                    CarResponse response = carMapper.toCarResponse(car);
                    if (car.getImageUrl() != null) {
                        String fileName = Paths.get(car.getImageUrl()).getFileName().toString();
                        response.setImageUrl(BASE_IMAGE_URL + fileName);
                    }
                    return response;
                })
                .toList();
    }


    public List<CarResponse> getAllCars() {
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .map(car -> {
                    CarResponse response = carMapper.toCarResponse(car);
                    if (car.getImageUrl() != null) {
                        String fileName = Paths.get(car.getImageUrl()).getFileName().toString();
                        response.setImageUrl(BASE_IMAGE_URL + fileName);
                    }
                    return response;
                })
                .toList();
    }
}