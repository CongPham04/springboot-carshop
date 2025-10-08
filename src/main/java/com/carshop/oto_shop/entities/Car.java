package com.carshop.oto_shop.entities;

import com.carshop.oto_shop.enums.Brand;
import com.carshop.oto_shop.enums.CarStatus;
import com.carshop.oto_shop.enums.Category;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Random;

@Entity
@Table(
        name = "cars"
)
public class Car {
    @Id
    @Column(name = "car_id", nullable = false, updatable = false)
    private Long carId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 20)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "brand", nullable = false, length = 20)
    private Brand brand;

    @Column(name = "model", nullable = false, length = 50)
    private String model;

    @Column(name = "manufacture_year", nullable = false)
    private Integer manufactureYear;

    @Column(name = "price", nullable = false, precision = 15, scale = 3)
    private BigDecimal price;

    @Column(name = "color", length = 30)
    private String color;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private CarStatus status;

    @Column(name = "image_url", length = 255)
    private String imageUrl;
    @PrePersist
    public void generateAuto(){
        if(this.status == null){
            this.status = CarStatus.AVAILABLE;
        }
        if(this.carId == null){
            Random random = new Random();
            this.carId = 100000L + random.nextLong(900000);
        }
    }

    public Car() {

    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
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
