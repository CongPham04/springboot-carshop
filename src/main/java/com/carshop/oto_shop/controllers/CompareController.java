package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.dto.car.CarResponse;
import com.carshop.oto_shop.services.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
public class CompareController {

    private final CarService carService;

    public CompareController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/compare")
    public ResponseEntity<List<CarResponse>> compareCars(@RequestParam List<Long> ids) {
        // A proper implementation would fetch details for the specified car IDs.
        List<CarResponse> cars = ids.stream()
                .map(carService::getCar)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cars);
    }
}
