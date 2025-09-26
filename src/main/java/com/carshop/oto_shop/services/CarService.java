package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.BadRequestException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.car.CarRequest;
import com.carshop.oto_shop.dto.car.CarResponse;
import com.carshop.oto_shop.entities.Car;
import com.carshop.oto_shop.entities.CarBrand;
import com.carshop.oto_shop.entities.CarCategory;
import com.carshop.oto_shop.mappers.CarMapper;
import com.carshop.oto_shop.repositories.CarBrandRepository;
import com.carshop.oto_shop.repositories.CarCategoryRepository;
import com.carshop.oto_shop.repositories.CarDetailRepository;
import com.carshop.oto_shop.repositories.CarRepository;
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


@Service
public class CarService {
    private static final Logger logger = LoggerFactory.getLogger(CarService.class);
    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final CarCategoryRepository carCategoryRepository;
    private final CarBrandRepository carBrandRepository;
    private final CarDetailRepository carDetailRepository;
    public static final String UPLOAD_DIR = "uploads/";
    private static final String BASE_IMAGE_URL = "http://localhost:8080/carshop/api/cars/image/";

    public CarService(CarRepository carRepository, CarMapper carMapper , CarCategoryRepository carCategoryRepository, CarBrandRepository carBrandRepository, CarDetailRepository carDetailRepository) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
        this.carCategoryRepository = carCategoryRepository;
        this.carBrandRepository = carBrandRepository;
        this.carDetailRepository = carDetailRepository;
    }

    public void createCar(CarRequest carRequest) {
        try{
            CarCategory carCategory = carCategoryRepository.findById(carRequest.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CARCATEGORY_NOT_FOUND));
            CarBrand carBrand = carBrandRepository.findById(carRequest.getBrandId())
                    .orElseThrow(() -> new AppException(ErrorCode.CARBRAND_NOT_FOUND));
            Car car = carMapper.toCar(carRequest);
            String imageUrl = null;
            if(carRequest.getImageFile() != null && !carRequest.getImageFile().isEmpty()) {
                imageUrl = saveImage(carRequest.getImageFile());
            }
            logger.info("Model: " + carRequest.getModel());
            car.setCarCategory(carCategory);
            car.setCarBrand(carBrand);
            car.setImageUrl(imageUrl);
            carRepository.save(car);
        }catch (DataIntegrityViolationException ex){
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
        try{
            // Kiểm tra MIME type(Multipurpose Internet Mail Extensions type)
            String contentType = file.getContentType();
            if (contentType == null ||
                    (!contentType.equals("image/png")
                            && !contentType.equals("image/jpeg")
                            && !contentType.equals("application/pdf"))) {
                throw new AppException(ErrorCode.UNSUPPORTED_MEDIA_TYPE);
            }
            //Tạo thư mục nếu chưa có
            File uploadDir = new File(UPLOAD_DIR) ;
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            //Tạo tên file
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            // Lưu file vào thư mục
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            // URL ảnh
            return "/uploads/" + fileName;
        }catch (IOException e){
            throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    public void deleteCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));
        // Xoá file ảnh vật lý nếu có
        deleteImageFile(car.getImageUrl());
        carDetailRepository.deleteAllByCarId(carId);
        // Xoá dữ liệu xe trong DB
        carRepository.delete(car);
    }

    public CarResponse getCar(Long carId){
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));
        CarResponse response = carMapper.toCarResponse(car);
        response.setBrandId(car.getCarBrand().getBrandId());
        response.setCategoryId(car.getCarCategory().getCategoryId());
        if (car.getImageUrl() != null) {
            // Thay "/uploads/xxx.png" thành full URL API
            String fileName = Paths.get(car.getImageUrl()).getFileName().toString();
            response.setImageUrl(BASE_IMAGE_URL + fileName);
        }
        return response;
    }

    public List<CarResponse> getCarsByBrand(Long brandId) {
        List<Car> cars = carRepository.findAllByCarBrand_BrandId(brandId);
        return cars.stream()
                .map(car -> {
                    CarResponse response = carMapper.toCarResponse(car);
                    response.setBrandId(car.getCarBrand().getBrandId());
                    response.setCategoryId(car.getCarCategory().getCategoryId());
                    if (car.getImageUrl() != null) {
                        String fileName = Paths.get(car.getImageUrl()).getFileName().toString();
                        response.setImageUrl(BASE_IMAGE_URL + fileName);
                    }
                    return response;
                })
                .toList();
    }

    public List<CarResponse> getCarsByCategory(Long categoryId) {
        List<Car> cars = carRepository.findAllByCarCategory_CategoryId(categoryId);
        return cars.stream()
                .map(car -> {
                    CarResponse response = carMapper.toCarResponse(car);
                    //Vì đối tượng car không có BrandId và CategoryId nên phải set cho nó
                    response.setBrandId(car.getCarBrand().getBrandId());
                    response.setCategoryId(car.getCarCategory().getCategoryId());
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
                    response.setBrandId(car.getCarBrand().getBrandId());
                    response.setCategoryId(car.getCarCategory().getCategoryId());
                    if (car.getImageUrl() != null) {
                        String fileName = Paths.get(car.getImageUrl()).getFileName().toString();
                        response.setImageUrl(BASE_IMAGE_URL + fileName);
                    }
                    return response;
                })
                .toList();
    }

}
