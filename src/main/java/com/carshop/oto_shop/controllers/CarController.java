package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.car.CarRequest;
import com.carshop.oto_shop.dto.car.CarResponse;
import com.carshop.oto_shop.enums.Brand;
import com.carshop.oto_shop.enums.Category;
import com.carshop.oto_shop.services.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "CarController")
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Operation(summary = "Add car", description = "API create new car with Brand and Category enums")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> createCar(@Valid @ModelAttribute CarRequest carRequest ) {
        carService.createCar(carRequest);
        return ResponseEntity.ok(ApiResponse.success("Thêm sản phẩm thành công!"));
    }

    @Operation(summary = "Update car", description = "API update car with Brand and Category enums")
    @PutMapping(value = "/{carId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> updateCar(@PathVariable("carId") Long carId, @Valid @ModelAttribute CarRequest carRequest ) {
        carService.updateCar(carRequest,carId);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật sản phẩm thành công!"));
    }

    @Operation(summary = "Delete Car", description = "API delete car")
    @DeleteMapping(value = "/{carId}")
    public ResponseEntity<ApiResponse<Void>> deleteCar(@PathVariable("carId") Long carId) {
        carService.deleteCar(carId);
        return ResponseEntity.ok(ApiResponse.success("Xoá sản phẩm thành công!"));
    }

    @Operation(summary = "Get car detail", description = "API get car detail")
    @GetMapping(value = "/{carId}")
    public ResponseEntity<ApiResponse<CarResponse>> getCar(@PathVariable("carId") Long carId) {
        CarResponse dataCars = carService.getCar(carId);
        return ResponseEntity.ok(ApiResponse.success("Lấy ra sản phẩm thành công!", dataCars));
    }

    @Operation(summary = "Get image", description = "API get image")
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

    @Operation(summary = "Get cars by brand", description = "API get all cars by Brand enum (TOYOTA, HYUNDAI, MERCEDES, VINFAST)")
    @GetMapping("/brand/{brand}")
    public ResponseEntity<ApiResponse<List<CarResponse>>> getCarsByBrand(@PathVariable("brand") Brand brand) {
        List<CarResponse> dataCars = carService.getCarsByBrand(brand);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách xe theo brand thành công!", dataCars));
    }

    @Operation(summary = "Get cars by category", description = "API get all cars by Category enum (SUV, SEDAN, HATCHBACK)")
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<CarResponse>>> getCarsByCategory(@PathVariable("category") Category category) {
        List<CarResponse> dataCars = carService.getCarsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách xe theo category thành công!", dataCars));
    }

    @Operation(summary = "Get all car", description = "API get all car")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CarResponse>>> getAllCars() {
        List<CarResponse> dataCars = carService.getAllCars();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sản phẩm thành công!", dataCars));
    }

}
