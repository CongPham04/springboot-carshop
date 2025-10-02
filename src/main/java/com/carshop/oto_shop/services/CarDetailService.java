package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.BadRequestException;
import com.carshop.oto_shop.common.exceptions.DuplicateKeyException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.cardetail.CarDetailRequest;
import com.carshop.oto_shop.dto.cardetail.CarDetailResponse;
import com.carshop.oto_shop.entities.Car;
import com.carshop.oto_shop.entities.CarDetail;
import com.carshop.oto_shop.mappers.CarDetailMapper;
import com.carshop.oto_shop.repositories.CarDetailRepository;
import com.carshop.oto_shop.repositories.CarRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarDetailService {
    private final CarDetailMapper carDetailMapper;
    private final CarDetailRepository carDetailRepository;
    private final CarRepository carRepository;
    private CarDetailRequest CarDetailRepository;

    public CarDetailService(CarDetailMapper carDetailMapper, CarDetailRepository carDetailRepository, CarRepository carRepository) {
        this.carDetailMapper = carDetailMapper;
        this.carDetailRepository = carDetailRepository;
        this.carRepository = carRepository;
    }

    public void createCarDetail(Long carId, CarDetailRequest carDetailRequest) {
        try{
            Car car = carRepository.findById(carId)
                    .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));
            CarDetail carDetail = carDetailMapper.toCarDetail(carDetailRequest);
            carDetail.setCar(car);
            carDetailRepository.save(carDetail);
        }catch(DataIntegrityViolationException e){
            String message = e.getMostSpecificCause().getMessage();
            if(message != null){
                if(message.contains("Duplicate entry")){
                    throw new DuplicateKeyException("Chi tiết xe cho id này đã tồn tại!");
                }else if (message.contains("cannot be null")) {
                    // Lấy ra tên cột bị null từ message (ví dụ: "Column 'password' cannot be null")
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

    public void updateCarDetail(Long carDetailId, CarDetailRequest carDetailRequest) {
        try{
            CarDetail carDetail = carDetailRepository.findById(carDetailId)
                    .orElseThrow(() -> new AppException(ErrorCode.CAR_DETAIL_NOT_FOUND));
            carDetailMapper.updateCarDetail(carDetailRequest, carDetail);
            carDetailRepository.save(carDetail);
        }catch (DataIntegrityViolationException e){
            String message = e.getMostSpecificCause().getMessage();
            if(message != null){
                if(message.contains("uk_car_details_car_id")){
                    throw new DuplicateKeyException("Chi tiết xe cho id này đã tồn tại!");
                }else if (message.contains("cannot be null")) {
                    // Lấy ra tên cột bị null từ message (ví dụ: "Column 'password' cannot be null")
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

    public void deleteCarDetail(Long carDetailId) {
        CarDetail carDetail = carDetailRepository.findById(carDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.CAR_DETAIL_NOT_FOUND));
        carDetailRepository.delete(carDetail);
    }

    public CarDetailResponse getCarDetail(Long detailId) {
        CarDetail carDetail = carDetailRepository.findById(detailId)
                .orElseThrow(() -> new AppException(ErrorCode.CAR_DETAIL_NOT_FOUND));
        CarDetailResponse carDetailResponse = carDetailMapper.toCarDetailResponse(carDetail);
        carDetailResponse.setCarId(carDetail.getCar().getCarId());
        return carDetailResponse;
    }

    public List<CarDetailResponse> getAllCarDetails() {
        List<CarDetail> carDetails = carDetailRepository.findAll();
        return carDetails.stream()
                .map(carDetail -> {
                    CarDetailResponse carDetailResponse = carDetailMapper.toCarDetailResponse(carDetail);
                    carDetailResponse.setCarId(carDetail.getCar().getCarId());
                    return carDetailResponse;
                })
                .toList();
    }
}
