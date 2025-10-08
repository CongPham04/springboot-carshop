package com.carshop.oto_shop.dto.car;

import com.carshop.oto_shop.enums.Brand;
import com.carshop.oto_shop.enums.CarStatus;
import com.carshop.oto_shop.enums.Category;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public class CarResponse {
    private Long carId;

    private Brand brand;

    private Category category;

    private String model;

    private Integer manufactureYear;

    private BigDecimal price;

    private String color;

    private String description;

    private CarStatus status;

    private String imageUrl;

    public CarResponse() {}

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(Integer manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CarStatus getStatus() {
        return status;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
