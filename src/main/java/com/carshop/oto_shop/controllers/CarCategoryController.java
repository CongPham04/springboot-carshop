package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.carcategory.CarCategoryRequest;
import com.carshop.oto_shop.dto.carcategory.CarCategoryResponse;
import com.carshop.oto_shop.services.CarCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/car-categories")
@Tag(name = "CarCategoryController")
public class CarCategoryController {
    private final CarCategoryService carCategoryService;
    public CarCategoryController(CarCategoryService carCategoryService) {
        this.carCategoryService = carCategoryService;
    }

    @Operation(summary = "Add car category", description = "API create new car category")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createCarCategory(@RequestBody CarCategoryRequest carCategoryRequest) {
        carCategoryService.createCarCategory(carCategoryRequest);
        return ResponseEntity.ok(ApiResponse.success("Thêm danh mục thành công!"));
    }

    @Operation(summary = "Update car category", description = "API update car category")
    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> updateCarCategory(@PathVariable Long categoryId, @RequestBody CarCategoryRequest carCategoryRequest) {
        carCategoryService.updateCarCategory(carCategoryRequest, categoryId);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật danh mục thành công!"));
    }

    @Operation(summary = "Delete car category", description = "API delete car category")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCarCategory(@PathVariable Long categoryId){
        carCategoryService.deleteCarCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Xoá danh mục thành công!"));
    }

    @Operation(summary = "Get car category detail", description = "API get car category detail")
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CarCategoryResponse>> getCarCategory(@PathVariable Long categoryId){
        CarCategoryResponse dataCarCategoryResponses = carCategoryService.getCarCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin danh mục thành công!", dataCarCategoryResponses));
    }

    @Operation(summary = "Get all car category", description = "API get all car category")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CarCategoryResponse>>> getAllCarCategory(){
        List<CarCategoryResponse> dataCarCategoryResponses = carCategoryService.getAllCarCategory();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách danh mục thành công!", dataCarCategoryResponses));
    }
}
