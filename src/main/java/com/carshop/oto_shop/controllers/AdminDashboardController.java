package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminDashboardController {

    private final OrderService orderService;

    public AdminDashboardController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getOverviewStats() {
        Map<String, Object> stats = new HashMap<>();
        // Basic stats - more complex calculations would be done in the service layer
        stats.put("totalOrders", orderService.getAllOrders().size());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/sales")
    public ResponseEntity<Map<String, Object>> getSalesStats() {
        // Placeholder
        return ResponseEntity.ok(new HashMap<>());
    }

    @GetMapping("/top-products")
    public ResponseEntity<Map<String, Object>> getTopProducts() {
        // Placeholder
        return ResponseEntity.ok(new HashMap<>());
    }
}
