package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.order.OrderRequest;
import com.carshop.oto_shop.dto.order.OrderResponse;
import com.carshop.oto_shop.dto.order.OrderUpdateRequest;
import com.carshop.oto_shop.enums.OrderStatus;
import com.carshop.oto_shop.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "OrderController")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create order", description = "API create new order with order details and payment")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(ApiResponse.success("Tạo đơn hàng thành công!", orderResponse));
    }

    @Operation(summary = "Get order by ID", description = "API get order detail by order ID")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable String orderId) {
        OrderResponse orderResponse = orderService.getOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin đơn hàng thành công!", orderResponse));
    }

    @Operation(summary = "Get all orders", description = "API get all orders")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách đơn hàng thành công!", orders));
    }

    @Operation(summary = "Get orders by user ID", description = "API get all orders by user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByUserId(@PathVariable String userId) {
        List<OrderResponse> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách đơn hàng của người dùng thành công!", orders));
    }

    @Operation(summary = "Get orders by status", description = "API get all orders by status (PENDING, CONFIRMED, SHIPPING, DELIVERED, CANCELLED, COMPLETED)")
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderResponse> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách đơn hàng theo trạng thái thành công!", orders));
    }

    @Operation(summary = "Update order", description = "API update order information (shipping address, contact, fees)")
    @PutMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrder(
            @PathVariable String orderId,
            @Valid @RequestBody OrderUpdateRequest orderUpdateRequest) {
        OrderResponse orderResponse = orderService.updateOrder(orderId, orderUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật đơn hàng thành công!", orderResponse));
    }

    @Operation(summary = "Update order status", description = "API update order status")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam OrderStatus status) {
        OrderResponse orderResponse = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái đơn hàng thành công!", orderResponse));
    }

    @Operation(summary = "Delete order", description = "API delete order")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable String orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success("Xoá đơn hàng thành công!"));
    }
}
