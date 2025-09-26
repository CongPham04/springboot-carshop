package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.cardetail.CarDetailRequest;
import com.carshop.oto_shop.dto.cardetail.CarDetailResponse;
import com.carshop.oto_shop.services.CarDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car-details")
@Tag(name = "CarDetailController")
public class CarDetailController {
    private final CarDetailService carDetailService;

    public CarDetailController(CarDetailService carDetailService) {
        this.carDetailService = carDetailService;
    }

    @Operation(summary = "Create car detail", description = "API create car detail for a car")
    @PostMapping("/{carId}")
    public ResponseEntity<ApiResponse<Void>> createCarDetail(@PathVariable("carId") Long carId, @RequestBody CarDetailRequest carDetailRequest) {
        carDetailService.createCarDetail(carId, carDetailRequest);
        return ResponseEntity.ok(ApiResponse.success("Thêm chi tiết xe thành công!"));
    }

    @Operation(summary = "Update car detail", description = "API update car detail")
    @PutMapping("/{carDetailId}")
    public ResponseEntity<ApiResponse<Void>> updateCarDetail(@PathVariable("carDetailId") Long carDetailId, @RequestBody CarDetailRequest carDetailRequest) {
        carDetailService.updateCarDetail(carDetailId, carDetailRequest);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật chi tiết xe thành công!"));
    }

    @Operation(summary = "Delete car detail", description = "API delete car detail")
    @DeleteMapping("/{carDetailId}")
    public ResponseEntity<ApiResponse<Void>> deleteCarDetail(@PathVariable("carDetailId") Long carDetailId) {
        carDetailService.deleteCarDetail(carDetailId);
        return ResponseEntity.ok(ApiResponse.success("Xoá chi tiết xe thành công!"));
    }

    @Operation(summary = "Get car detail", description = "API get car detail by ID")
    @GetMapping("/{carDetailId}")
    public ResponseEntity<ApiResponse<CarDetailResponse>> getCarDetail(@PathVariable("carDetailId") Long carDetailId) {
        CarDetailResponse dataCarDetailResponses = carDetailService.getCarDetail(carDetailId);
        return ResponseEntity.ok(ApiResponse.success("Lấy chi tiết xe thành công!", dataCarDetailResponses));
    }

    @Operation(summary = "Get all car details", description = "API get all car details")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CarDetailResponse>>> getAllCarDetails() {
        List<CarDetailResponse> dataCarDetailResponses = carDetailService.getAllCarDetails();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách chi tiết xe thành công!", dataCarDetailResponses));
    }
}
