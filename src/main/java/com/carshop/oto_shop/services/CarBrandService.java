package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.BadRequestException;
import com.carshop.oto_shop.common.exceptions.DuplicateKeyException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.carbrand.CarBrandRequest;
import com.carshop.oto_shop.dto.carbrand.CarBrandResponse;
import com.carshop.oto_shop.entities.Car;
import com.carshop.oto_shop.entities.CarBrand;
import com.carshop.oto_shop.mappers.CarBrandMapper;
import com.carshop.oto_shop.repositories.CarBrandRepository;
import com.carshop.oto_shop.repositories.CarRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class CarBrandService {
    private static final Logger logger = LoggerFactory.getLogger(CarBrandService.class);

    private final CarBrandRepository carBrandRepository;
    private final CarBrandMapper carBrandMapper;
    private final CarRepository carRepository;

    public CarBrandService(CarBrandRepository carBrandRepository,
                           CarBrandMapper carBrandMapper,
                           CarRepository carRepository) {
        this.carBrandRepository = carBrandRepository;
        this.carBrandMapper = carBrandMapper;
        this.carRepository = carRepository;
    }

    public void createCarBrand(CarBrandRequest request) {
        try {
            CarBrand carBrand = carBrandMapper.toCarBrand(request);
            carBrandRepository.save(carBrand);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            if (message != null) {
                if (message.contains("uk_brands_name")) {
                    throw new DuplicateKeyException("Tên thương hiệu đã tồn tại!");
                } else if (message.contains("cannot be null")) {
                    String field = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));
                    throw new BadRequestException(field + " không được để trống!");
                } else {
                    throw new AppException(ErrorCode.BAD_REQUEST);
                }
            } else {
                throw new AppException(ErrorCode.UNKNOWN);
            }
        }
    }

    public void updateCarBrand(Long brandId, CarBrandRequest request) {
        try {
            CarBrand carBrand = carBrandRepository.findById(brandId)
                    .orElseThrow(() -> new AppException(ErrorCode.CARBRAND_NOT_FOUND));
            carBrandMapper.updateCarBrandFromRequest(request, carBrand);
            carBrandRepository.save(carBrand);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            if (message != null) {
                if (message.contains("uk_brands_name")) {
                    throw new DuplicateKeyException("Tên thương hiệu đã tồn tại!");
                } else if (message.contains("cannot be null")) {
                    String field = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));
                    throw new BadRequestException(field + " không được để trống!");
                } else {
                    throw new AppException(ErrorCode.BAD_REQUEST);
                }
            } else {
                throw new AppException(ErrorCode.UNKNOWN);
            }
        }
    }

    private void deleteImageFile(String imageUrl) {
        if (imageUrl == null) return;
        try {
            String fileName = Paths.get(imageUrl).getFileName().toString();
            Path filePath = Paths.get(CarService.UPLOAD_DIR).resolve(fileName).normalize();
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
    public void deleteCarBrand(Long brandId) {
        CarBrand carBrand = carBrandRepository.findById(brandId)
                .orElseThrow(() -> new AppException(ErrorCode.CARBRAND_NOT_FOUND));
        // NOTE: Car now uses Brand enum instead of CarBrand entity relationship
        // No need to delete related cars since brand is now an enum field
        // List<Car> cars = carRepository.findAllByCarBrand_BrandId(brandId); // OLD METHOD - REMOVED
        carBrandRepository.delete(carBrand);
    }

    public CarBrandResponse getCarBrand(Long brandId) {
        CarBrand carBrand = carBrandRepository.findById(brandId)
                .orElseThrow(() -> new AppException(ErrorCode.CARBRAND_NOT_FOUND));
        return carBrandMapper.toCarBrandResponse(carBrand);
    }

    public List<CarBrandResponse> getAllCarBrands() {
        List<CarBrand> carBrandList = carBrandRepository.findAll();
        return carBrandList.stream()
                .map(carBrandMapper::toCarBrandResponse)
                .toList();
    }
}
