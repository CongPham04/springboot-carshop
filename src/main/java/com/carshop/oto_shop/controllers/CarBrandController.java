package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.carbrand.CarBrandRequest;
import com.carshop.oto_shop.dto.carbrand.CarBrandResponse;
import com.carshop.oto_shop.services.CarBrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car-brands")
@Tag(name = "CarBrandController")
public class CarBrandController {
    private final CarBrandService carBrandService;
    public CarBrandController(CarBrandService carBrandService) {
        this.carBrandService = carBrandService;
    }

    @Operation(summary = "Add car brand", description = "API create new car brand")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createCarBrand(@RequestBody CarBrandRequest carBrandRequest) {
        carBrandService.createCarBrand(carBrandRequest);
        return ResponseEntity.ok(ApiResponse.success("Thêm thương hiệu thành công!"));
    }

    @Operation(summary = "Update car brand", description = "API update car brand")
    @PutMapping("/{brandId}")
    public ResponseEntity<ApiResponse<Void>> updateCarBrand(@PathVariable Long brandId, @RequestBody CarBrandRequest carBrandRequest) {
        carBrandService.updateCarBrand(brandId, carBrandRequest);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thương hiệu thành công!"));
    }

    @Operation(summary = "Delete car brand", description = "API delete car brand")
    @DeleteMapping("/{brandId}")
    public ResponseEntity<ApiResponse<Void>> deleteCarBrand(@PathVariable Long brandId) {
        carBrandService.deleteCarBrand(brandId);
        return ResponseEntity.ok(ApiResponse.success("Xoá thương hiệu thành công!"));
    }

    @Operation(summary = "Get car brand detail", description = "API get car brand detail")
    @GetMapping("/{brandId}")
    public ResponseEntity<ApiResponse<CarBrandResponse>> getCarBrand(@PathVariable Long brandId) {
        CarBrandResponse dataCarBrandRespones = carBrandService.getCarBrand(brandId);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin thương hiệu thành công!", dataCarBrandRespones));
    }

    @Operation(summary = "Get all car brands", description = "API get all car brands")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CarBrandResponse>>> getAllCarBrands() {
        List<CarBrandResponse> dataCarBrandRespones = carBrandService.getAllCarBrands();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách thương hiệu thành công!", dataCarBrandRespones));
    }
}
