package com.carshop.oto_shop.dto.orderdetail;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderDetailRequest {

    @NotNull(message = "Car ID không được để trống")
    private Long carId;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    // Getters and Setters
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
