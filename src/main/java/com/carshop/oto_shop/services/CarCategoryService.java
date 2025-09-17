package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.BadRequestException;
import com.carshop.oto_shop.common.exceptions.DuplicateKeyException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.carcategory.CarCategoryRequest;
import com.carshop.oto_shop.dto.carcategory.CarCategoryResponse;
import com.carshop.oto_shop.entities.CarCategory;
import com.carshop.oto_shop.mappers.CarCategoryMapper;
import com.carshop.oto_shop.repositories.CarCategoryRepository;
import com.carshop.oto_shop.repositories.CarRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarCategoryService {
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

    @Transactional
    public void deleteCarCategory(Long categoryId) {
        CarCategory carCategory = carCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CARCATEGORY_NOT_FOUND));
        carRepository.deleteByCategoryId(categoryId);
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
