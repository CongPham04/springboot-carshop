package com.carshop.oto_shop.controllers;

import com.carshop.oto_shop.common.response.ApiResponse;
import com.carshop.oto_shop.dto.payment.PaymentRequest;
import com.carshop.oto_shop.dto.payment.PaymentResponse;
import com.carshop.oto_shop.enums.PaymentStatus;
import com.carshop.oto_shop.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "PaymentController")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Create payment", description = "API create new payment for an order")
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = paymentService.createPayment(paymentRequest);
        return ResponseEntity.ok(ApiResponse.success("Tạo thanh toán thành công!", paymentResponse));
    }

    @Operation(summary = "Get payment by ID", description = "API get payment detail by payment ID")
    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(@PathVariable String paymentId) {
        PaymentResponse paymentResponse = paymentService.getPayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin thanh toán thành công!", paymentResponse));
    }

    @Operation(summary = "Get payment by order ID", description = "API get payment by order ID")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByOrderId(@PathVariable String orderId) {
        PaymentResponse paymentResponse = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin thanh toán theo đơn hàng thành công!", paymentResponse));
    }

    @Operation(summary = "Get all payments", description = "API get all payments")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách thanh toán thành công!", payments));
    }

    @Operation(summary = "Get payments by status", description = "API get all payments by status (PENDING, SUCCESS, FAILED)")
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<PaymentResponse> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách thanh toán theo trạng thái thành công!", payments));
    }

    @Operation(summary = "Update payment status", description = "API update payment status")
    @PatchMapping("/{paymentId}/status")
    public ResponseEntity<ApiResponse<PaymentResponse>> updatePaymentStatus(
            @PathVariable String paymentId,
            @RequestParam PaymentStatus status) {
        PaymentResponse paymentResponse = paymentService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái thanh toán thành công!", paymentResponse));
    }

    @Operation(summary = "Delete payment", description = "API delete payment")
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<Void>> deletePayment(@PathVariable String paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success("Xoá thanh toán thành công!"));
    }
}
