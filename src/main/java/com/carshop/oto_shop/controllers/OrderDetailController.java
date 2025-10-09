package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.orderdetail.OrderDetailResponse;
import com.carshop.oto_shop.services.OrderDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-details")
@Tag(name = "OrderDetailController")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @Operation(summary = "Get order detail by ID", description = "API get order detail by order detail ID")
    @GetMapping("/{orderDetailId}")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrderDetail(@PathVariable Long orderDetailId) {
        OrderDetailResponse orderDetailResponse = orderDetailService.getOrderDetail(orderDetailId);
        return ResponseEntity.ok(ApiResponse.success("Lấy chi tiết đơn hàng thành công!", orderDetailResponse));
    }

    @Operation(summary = "Get order details by order ID", description = "API get all order details by order ID")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<OrderDetailResponse>>> getOrderDetailsByOrderId(@PathVariable String orderId) {
        List<OrderDetailResponse> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách chi tiết đơn hàng thành công!", orderDetails));
    }

    @Operation(summary = "Get all order details", description = "API get all order details")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDetailResponse>>> getAllOrderDetails() {
        List<OrderDetailResponse> orderDetails = orderDetailService.getAllOrderDetails();
        return ResponseEntity.ok(ApiResponse.success("Lấy tất cả chi tiết đơn hàng thành công!", orderDetails));
    }

    @Operation(summary = "Delete order detail", description = "API delete order detail")
    @DeleteMapping("/{orderDetailId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrderDetail(@PathVariable Long orderDetailId) {
        orderDetailService.deleteOrderDetail(orderDetailId);
        return ResponseEntity.ok(ApiResponse.success("Xoá chi tiết đơn hàng thành công!"));
    }
}
