package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.BadRequestException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.car.CarRequest;
import com.carshop.oto_shop.entities.Car;
import com.carshop.oto_shop.entities.CarCategory;
import com.carshop.oto_shop.mappers.CarMapper;
import com.carshop.oto_shop.repositories.CarCategoryRepository;
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


@Service
public class CarService {
    private static final Logger logger = LoggerFactory.getLogger(CarService.class);
    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final CarCategoryRepository carCategoryRepository;
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public CarService(CarRepository carRepository, CarMapper carMapper , CarCategoryRepository carCategoryRepository) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
        this.carCategoryRepository = carCategoryRepository;
    }

    public void CreateCar(CarRequest carRequest, Long categoryId) {
        try{
            CarCategory carCategory = carCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new AppException(ErrorCode.CARCATEGORY_NOT_FOUND));
            Car car = carMapper.toCar(carRequest);
            String imageUrl = null;
            if(carRequest.getImageFile() != null && !carRequest.getImageFile().isEmpty()) {
                imageUrl = saveImage(carRequest.getImageFile());
            }
            logger.info("Brand: " + carRequest.getBrand());
            logger.info("Model: " + carRequest.getModel());
            car.setCarCategory(carCategory);
            car.setImageUrl(imageUrl);

            carRepository.save(car);
        }catch (DataIntegrityViolationException ex){
            String message = ex.getMostSpecificCause().getMessage();
            if(message != null){
                if(message.contains("cannot be null")){
                    String field = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));
                    throw new BadRequestException(field + " không được để trống!");
                }
            }else{
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
            //Lưu file
            Files.write(path,file.getBytes());
            // URL ảnh
            return "/uploads/" + fileName;
        }catch (IOException e){
            throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }


}
