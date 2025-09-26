package com.carshop.oto_shop.entities;

import jakarta.persistence.*;

import java.util.Random;

@Entity
@Table(
        name = "car_brands",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_brands_name", columnNames = "brand_name")
        }
)
public class CarBrand {
    @Id
    @Column(name = "brand_id", nullable = false, updatable = false)
    private Long brandId;

    @Column(name = "brand_name", nullable = false, unique = true, length = 100)
    private String brandName;

    @PrePersist
    public void generateId(){
        if(this.brandId == null){
            Random random = new Random();
            this.brandId = 100000L + random.nextLong(900000);
        }

    }
    public CarBrand() {}

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
