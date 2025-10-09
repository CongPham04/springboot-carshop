package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.BadRequestException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.order.OrderRequest;
import com.carshop.oto_shop.dto.order.OrderResponse;
import com.carshop.oto_shop.dto.order.OrderUpdateRequest;
import com.carshop.oto_shop.dto.orderdetail.OrderDetailRequest;
import com.carshop.oto_shop.entities.*;
import com.carshop.oto_shop.enums.OrderStatus;
import com.carshop.oto_shop.mappers.OrderMapper;
import com.carshop.oto_shop.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository,
                       OrderDetailRepository orderDetailRepository,
                       PaymentRepository paymentRepository,
                       UserRepository userRepository,
                       CarRepository carRepository,
                       OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        try {
            // Validate user exists
            User user = userRepository.findById(orderRequest.getUserId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            // Create order
            Order order = orderMapper.toOrder(orderRequest);
            order.setUser(user);

            // Set default values for fees if null
            if (order.getShippingFee() == null) {
                order.setShippingFee(BigDecimal.ZERO);
            }
            if (order.getTax() == null) {
                order.setTax(BigDecimal.ZERO);
            }

            // Create order details and calculate subtotal
            List<OrderDetail> orderDetails = new ArrayList<>();
            BigDecimal subtotal = BigDecimal.ZERO;

            for (OrderDetailRequest detailRequest : orderRequest.getOrderDetails()) {
                Car car = carRepository.findById(detailRequest.getCarId())
                        .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setCar(car);
                orderDetail.setQuantity(detailRequest.getQuantity());
                orderDetail.setPrice(car.getPrice()); // Snapshot price at order time

                BigDecimal itemTotal = car.getPrice().multiply(BigDecimal.valueOf(detailRequest.getQuantity()));
                subtotal = subtotal.add(itemTotal);

                orderDetails.add(orderDetail);
            }

            order.setSubtotal(subtotal);
            order.setTotalAmount(subtotal.add(order.getShippingFee()).add(order.getTax()));
            order.setOrderDetails(orderDetails);

            // Create payment
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setAmount(order.getTotalAmount());
            payment.setPaymentMethod(orderRequest.getPaymentMethod());
            order.setPayment(payment);

            // Save order (cascade will save order details and payment)
            Order savedOrder = orderRepository.save(order);

            logger.info("Created order {} for user {} with total amount {}",
                    savedOrder.getOrderId(), user.getUserId(), savedOrder.getTotalAmount());

            return orderMapper.toOrderResponse(savedOrder);
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
            logger.error("Error creating order: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNKNOWN);
        }
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toOrderResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(String userId) {
        List<Order> orders = orderRepository.findByUser_UserId(userId);
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findByStatus(status);
        return orders.stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Transactional
    public OrderResponse updateOrder(String orderId, OrderUpdateRequest orderUpdateRequest) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

            // Update shipping information if provided
            if (orderUpdateRequest.getFullName() != null) {
                order.setFullName(orderUpdateRequest.getFullName());
            }
            if (orderUpdateRequest.getEmail() != null) {
                order.setEmail(orderUpdateRequest.getEmail());
            }
            if (orderUpdateRequest.getPhone() != null) {
                order.setPhone(orderUpdateRequest.getPhone());
            }
            if (orderUpdateRequest.getAddress() != null) {
                order.setAddress(orderUpdateRequest.getAddress());
            }
            if (orderUpdateRequest.getCity() != null) {
                order.setCity(orderUpdateRequest.getCity());
            }
            if (orderUpdateRequest.getDistrict() != null) {
                order.setDistrict(orderUpdateRequest.getDistrict());
            }
            if (orderUpdateRequest.getWard() != null) {
                order.setWard(orderUpdateRequest.getWard());
            }
            if (orderUpdateRequest.getNote() != null) {
                order.setNote(orderUpdateRequest.getNote());
            }

            // Update financial information if provided
            if (orderUpdateRequest.getShippingFee() != null) {
                order.setShippingFee(orderUpdateRequest.getShippingFee());
            }
            if (orderUpdateRequest.getTax() != null) {
                order.setTax(orderUpdateRequest.getTax());
            }

            // Recalculate total amount if shipping fee or tax changed
            if (orderUpdateRequest.getShippingFee() != null || orderUpdateRequest.getTax() != null) {
                order.setTotalAmount(order.getSubtotal().add(order.getShippingFee()).add(order.getTax()));
            }

            Order updatedOrder = orderRepository.save(order);
            logger.info("Updated order {} information", orderId);

            return orderMapper.toOrderResponse(updatedOrder);
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
            logger.error("Error updating order: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNKNOWN);
        }
    }

    @Transactional
    public OrderResponse updateOrderStatus(String orderId, OrderStatus status) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

            order.setStatus(status);
            Order updatedOrder = orderRepository.save(order);

            logger.info("Updated order {} status to {}", orderId, status);
            return orderMapper.toOrderResponse(updatedOrder);
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
            logger.error("Error updating order status: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNKNOWN);
        }
    }

    @Transactional
    public void deleteOrder(String orderId) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

            orderRepository.delete(order);
            logger.info("Deleted order {}", orderId);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            logger.error("DataIntegrityViolationException caught: {}", message);

            if (message != null) {
                throw new AppException(ErrorCode.BAD_REQUEST);
            } else {
                throw new AppException(ErrorCode.UNKNOWN);
            }
        } catch (Exception e) {
            logger.error("Error deleting order: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNKNOWN);
        }
    }
}
