package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.services.CarService;
import com.carshop.oto_shop.services.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    private final CarService carService;
    private final NewsService newsService;

    public HomeController(CarService carService, NewsService newsService) {
        this.carService = carService;
        this.newsService = newsService;
    }

    @GetMapping("/sections")
    public ResponseEntity<Map<String, Object>> getHomeSections() {
        Map<String, Object> sections = new HashMap<>();
        // These service methods would need to be implemented with business logic
        // For now, they might return all cars/news or a subset.
        sections.put("newArrivals", carService.getAllCars()); // Placeholder logic
        sections.put("latestNews", newsService.getAllNews());   // Placeholder logic
        // Other sections like featured brands, special offers can be added here.
        return ResponseEntity.ok(sections);
    }
}
