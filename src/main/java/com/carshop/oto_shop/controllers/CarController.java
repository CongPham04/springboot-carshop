package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.car.CarRequest;
import com.carshop.oto_shop.dto.car.CarResponse;
import com.carshop.oto_shop.services.CarService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping(value = "/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> createCar(@PathVariable("categoryId") Long categoryId, @ModelAttribute CarRequest carRequest ) {
        carService.createCar(carRequest, categoryId);
        return ResponseEntity.ok(ApiResponse.success("Thêm sản phẩm thành công!"));
    }

    @PutMapping(value = "/{carId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> updateCar(@PathVariable("carId") Long carId, @ModelAttribute CarRequest carRequest ) {
        carService.updateCar(carRequest,carId);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật sản phẩm thành công!"));
    }

    @DeleteMapping(value = "/{carId}")
    public ResponseEntity<ApiResponse<Void>> deleteCar(@PathVariable("carId") Long carId) {
        carService.deleteCar(carId);
        return ResponseEntity.ok(ApiResponse.success("Xoá sản phẩm thành công!"));
    }

    @GetMapping(value = "/{carId}")
    public ResponseEntity<ApiResponse<CarResponse>> getCar(@PathVariable("carId") Long carId) {
        CarResponse dataCars = carService.getCar(carId);
        return ResponseEntity.ok(ApiResponse.success("Lấy ra sản phẩm thành công!", dataCars));
    }

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(CarService.UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Xác định Content-Type của file
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                throw new AppException(ErrorCode.FILE_NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            throw new AppException(ErrorCode.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CarResponse>>> getAllCars() {
        List<CarResponse> dataCars = carService.getAllCars();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sản phẩm thành công!", dataCars));
    }
}
