package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.dto.car.CarResponse;
import com.carshop.oto_shop.services.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final CarService carService;

    public SearchController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/cars")
    public ResponseEntity<List<CarResponse>> searchCars(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Double priceMin,
            @RequestParam(required = false) Double priceMax,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo
    ) {
        // [SỬA LỖI]
        // Thay vì gọi getAllCars(),
        // hãy gọi phương thức searchCars mới và truyền tất cả tham số vào.
        List<CarResponse> cars = carService.searchCars(
                keyword, brand, category, color, priceMin, priceMax, yearFrom, yearTo
        );

        return ResponseEntity.ok(cars);
    }
}