package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.BadRequestException;
import com.carshop.oto_shop.common.exceptions.DuplicateKeyException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.carcategory.CarCategoryRequest;
import com.carshop.oto_shop.dto.carcategory.CarCategoryResponse;
import com.carshop.oto_shop.entities.Car;
import com.carshop.oto_shop.entities.CarCategory;
import com.carshop.oto_shop.mappers.CarCategoryMapper;
import com.carshop.oto_shop.repositories.CarCategoryRepository;
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
public class CarCategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CarService.class);
    private final CarCategoryRepository carCategoryRepository;
    private final CarCategoryMapper carCategoryMapper;
    private final CarRepository carRepository;
    public CarCategoryService(CarCategoryRepository carCategoryRepository, CarCategoryMapper carCategoryMapper, CarRepository carRepository) {
        this.carCategoryRepository = carCategoryRepository;
        this.carCategoryMapper = carCategoryMapper;
        this.carRepository = carRepository;
    }
    public void createCarCategory(CarCategoryRequest carCategoryRequest) {
        try{
            CarCategory carCategory = carCategoryMapper.toCarCategory(carCategoryRequest);
            carCategoryRepository.save(carCategory);
        }catch (DataIntegrityViolationException e){
            String message = e.getMostSpecificCause().getMessage();
            if(message != null){
                if(message.contains("uk_categories_name")){
                    throw new DuplicateKeyException("tên danh mục đã tồn tại!");
                }else if(message.contains("cannot be null")){
                    String field = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));
                    throw new BadRequestException(field + " không được để trống!");
                }else{
                    throw new AppException(ErrorCode.BAD_REQUEST);
                }
            }else{
                throw new AppException(ErrorCode.UNKNOWN);
            }
        }
    }

    public void updateCarCategory(CarCategoryRequest carCategoryRequest, Long categoryId) {
        try{
            CarCategory carCategory = carCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new AppException(ErrorCode.CARCATEGORY_NOT_FOUND));
            carCategoryMapper.updateCarCategoryRequest(carCategoryRequest, carCategory);
            carCategoryRepository.save(carCategory);
        }catch(DataIntegrityViolationException e){
            String message = e.getMostSpecificCause().getMessage();
            if(message != null){
                if(message.contains("uk_categories_name")){
                    throw new DuplicateKeyException("tên danh mục đã tồn tại!");
                }else if(message.contains("cannot be null")){
                    String field = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));
                    throw new BadRequestException(field + " không được để trống!");
                }else{
                    throw new AppException(ErrorCode.BAD_REQUEST);
                }
            }else{
                throw new AppException(ErrorCode.UNKNOWN);
            }
        }
    }

    private void deleteImageFile(String imageUrl) {
        if (imageUrl == null)
            return;
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
    public void deleteCarCategory(Long categoryId) {
        CarCategory carCategory = carCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CARCATEGORY_NOT_FOUND));
        // Lấy tất cả Car thuộc category này
        List<Car> cars = carRepository.findAllByCarCategory_CategoryId(categoryId);
        if (!cars.isEmpty()) {
            for (Car car : cars) {
                if (car.getImageUrl() != null && !car.getImageUrl().isBlank()) {
                    deleteImageFile(car.getImageUrl());
                }
            }
            // Xoá toàn bộ car thuộc categoryId
            carRepository.deleteAll(cars);
        }
        carCategoryRepository.delete(carCategory);
    }

    public CarCategoryResponse getCarCategory(Long categoryId) {
        CarCategory carCategory = carCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CARCATEGORY_NOT_FOUND));
        return carCategoryMapper.toCarCategoryResponse(carCategory);
    }

    public List<CarCategoryResponse> getAllCarCategory(){
        List<CarCategory> carCategoryList = carCategoryRepository.findAll();
        return carCategoryList.stream()
                .map(carCategoryMapper::toCarCategoryResponse)
                .toList();
    }
}
