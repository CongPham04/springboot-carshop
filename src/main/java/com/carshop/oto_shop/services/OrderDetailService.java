package com.carshop.oto_shop.services;

import com.carshop.oto_shop.common.exceptions.AppException;
import com.carshop.oto_shop.common.exceptions.BadRequestException;
import com.carshop.oto_shop.common.exceptions.ErrorCode;
import com.carshop.oto_shop.dto.orderdetail.OrderDetailResponse;
import com.carshop.oto_shop.entities.OrderDetail;
import com.carshop.oto_shop.mappers.OrderDetailMapper;
import com.carshop.oto_shop.repositories.OrderDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderDetailService {
    private static final Logger logger = LoggerFactory.getLogger(OrderDetailService.class);

    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;

    public OrderDetailService(OrderDetailRepository orderDetailRepository,
                            OrderDetailMapper orderDetailMapper) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderDetailMapper = orderDetailMapper;
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(Long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_FOUND));
        return orderDetailMapper.toOrderDetailResponse(orderDetail);
    }

    @Transactional(readOnly = true)
    public List<OrderDetailResponse> getOrderDetailsByOrderId(String orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder_OrderId(orderId);
        return orderDetails.stream()
                .map(orderDetailMapper::toOrderDetailResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderDetailResponse> getAllOrderDetails() {
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        return orderDetails.stream()
                .map(orderDetailMapper::toOrderDetailResponse)
                .toList();
    }

    @Transactional
    public void deleteOrderDetail(Long orderDetailId) {
        try {
            OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_FOUND));

            orderDetailRepository.delete(orderDetail);
            logger.info("Deleted order detail {}", orderDetailId);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();
            logger.error("DataIntegrityViolationException caught: {}", message);

            if (message != null) {
                throw new AppException(ErrorCode.BAD_REQUEST);
            } else {
                throw new AppException(ErrorCode.UNKNOWN);
            }
        } catch (Exception e) {
            logger.error("Error deleting order detail: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNKNOWN);
        }
    }
}
