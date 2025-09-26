package com.carshop.oto_shop.entities;

import jakarta.persistence.*;

import java.util.Random;

@Entity
@Table(
        name = "car_details",
        uniqueConstraints = {
        @UniqueConstraint(name = "uk_car_details_car_id", columnNames = "car_id")
}
)
public class CarDetail {
    @Id
    @Column(name = "car_detail_id")
    private Long carDetailId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_car_details_car")
    )
    private Car car;

    @Column(name = "engine", nullable = false, length = 100)
    private String engine; // Động cơ

    @Column(name = "horsepower", nullable = false)
    private Integer horsepower; // Mã lực

    @Column(name = "torque", nullable = false)
    private Integer torque; // Mô-men xoắn (Nm)

    @Column(name = "transmission", nullable = false, length = 50)
    private String transmission; // Hộp số (MT, AT, CVT...)

    @Column(name = "fuel_type", nullable = false, length = 30)
    private String fuelType; // Loại nhiên liệu (xăng, dầu, điện...)

    @Column(name = "fuel_consumption", nullable = false, length = 50)
    private String fuelConsumption; // Ví dụ: 6.5L/100km

    @Column(name = "seats", nullable = false)
    private Integer seats; // Chỗ ngồi

    @Column(name = "weight", nullable = false)
    private Double weight; // Trọng lượng

    @Column(name = "dimensions", nullable = false, length = 100)
    private String dimensions; // Kích thước Ví dụ: 4500 x 1800 x 1450 mm

    @PrePersist
    public void generateId(){
        if(this.carDetailId == null){
            Random random = new Random();
            this.carDetailId = 100000000L + random.nextLong(900000000);
        }

    }

    public CarDetail() {}

    public Long getCarDetailId() {
        return carDetailId;
    }

    public void setCarDetailId(Long carDetailId) {
        this.carDetailId = carDetailId;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public Integer getHorsepower() {
        return horsepower;
    }

    public void setHorsepower(Integer horsepower) {
        this.horsepower = horsepower;
    }

    public Integer getTorque() {
        return torque;
    }

    public void setTorque(Integer torque) {
        this.torque = torque;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(String fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }
}
