package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.BadRequestException;
import com.carshop.oto_shop.common.exceptions.DuplicateKeyException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.payment.PaymentRequest;
import com.carshop.oto_shop.dto.payment.PaymentResponse;
import com.carshop.oto_shop.entities.Order;
import com.carshop.oto_shop.entities.Payment;
import com.carshop.oto_shop.enums.PaymentStatus;
import com.carshop.oto_shop.mappers.PaymentMapper;
import com.carshop.oto_shop.repositories.OrderRepository;
import com.carshop.oto_shop.repositories.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    public PaymentService(PaymentRepository paymentRepository,
                        OrderRepository orderRepository,
                        PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentMapper = paymentMapper;
    }

    @Transactional
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {

        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // Check if payment already exists for this order
        paymentRepository.findByOrder_OrderId(paymentRequest.getOrderId())
                .ifPresent(existingPayment -> {
                    throw new DuplicateKeyException("Đơn hàng này đã có thanh toán! Sử dụng API cập nhật trạng thái thanh toán thay vì tạo mới.");
                });

        Payment payment = paymentMapper.toPayment(paymentRequest);
        payment.setOrder(order);

        Payment savedPayment = paymentRepository.save(payment);
        logger.info("Created payment {} for order {}", savedPayment.getPaymentId(), order.getOrderId());

        return paymentMapper.toPaymentResponse(savedPayment);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        return paymentMapper.toPaymentResponse(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(String orderId) {
        Payment payment = paymentRepository.findByOrder_OrderId(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        return paymentMapper.toPaymentResponse(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
        List<Payment> payments = paymentRepository.findByStatus(status);
        return payments.stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }

    @Transactional
    public PaymentResponse updatePaymentStatus(String paymentId, PaymentStatus status) {
        try {
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

            payment.setStatus(status);

            // Set payment date if payment is successful
            if (status == PaymentStatus.SUCCESS && payment.getPaymentDate() == null) {
                payment.setPaymentDate(LocalDateTime.now());
            }

            Payment updatedPayment = paymentRepository.save(payment);
            logger.info("Updated payment {} status to {}", paymentId, status);

            return paymentMapper.toPaymentResponse(updatedPayment);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            logger.error("DataIntegrityViolationException caught: {}", message);

            if (message != null) {
                if (message.contains("cannot be null")) {
                    String field = message.substring(message.indexOf("'") + 1, message.lastIndexOf("'"));
                    throw new BadRequestException(field + " không được để trống!");
                } else {
                    throw new AppException(ErrorCode.BAD_REQUEST);
                }
            } else {
                throw new AppException(ErrorCode.UNKNOWN);
            }
        } catch (Exception e) {
            logger.error("Error updating payment status: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNKNOWN);
        }
    }

    @Transactional
    public PaymentResponse confirmPayment(String paymentId) {
        try {
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaymentDate(LocalDateTime.now());

            // Business rule: When payment is confirmed, update the order status
            Order order = payment.getOrder();
            if (order != null) {
                order.setStatus(com.carshop.oto_shop.enums.OrderStatus.CONFIRMED);
                orderRepository.save(order);
                logger.info("Updated order {} status to CONFIRMED due to payment confirmation", order.getOrderId());
            }

            Payment updatedPayment = paymentRepository.save(payment);
            logger.info("Confirmed payment {}", paymentId);

            return paymentMapper.toPaymentResponse(updatedPayment);
        } catch (Exception e) {
            logger.error("Error confirming payment: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNKNOWN);
        }
    }

    @Transactional
    public PaymentResponse failPayment(String paymentId) {
        try {
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

            payment.setStatus(PaymentStatus.FAILED);
            Payment updatedPayment = paymentRepository.save(payment);
            logger.warn("Marked payment {} as FAILED", paymentId);

            return paymentMapper.toPaymentResponse(updatedPayment);
        } catch (Exception e) {
            logger.error("Error failing payment: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNKNOWN);
        }
    }

    @Transactional
    public void deletePayment(String paymentId) {
        try {
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

            paymentRepository.delete(payment);
            logger.info("Deleted payment {}", paymentId);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            logger.error("DataIntegrityViolationException caught: {}", message);

            if (message != null) {
                throw new AppException(ErrorCode.BAD_REQUEST);
            } else {
                throw new AppException(ErrorCode.UNKNOWN);
            }
        } catch (Exception e) {
            logger.error("Error deleting payment: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNKNOWN);
        }
    }
}
