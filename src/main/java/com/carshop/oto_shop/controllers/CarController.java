package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.car.CarRequest;
import com.carshop.oto_shop.services.CarService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping(value = "/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> createCar(@PathVariable("categoryId") Long categoryId, @ModelAttribute CarRequest carRequest ) {
        carService.CreateCar(carRequest, categoryId);
        return ResponseEntity.ok(ApiResponse.success("Thêm sản phẩm thành công!"));
    }

}
