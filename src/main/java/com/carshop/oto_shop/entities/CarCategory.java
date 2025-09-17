package com.carshop.oto_shop.entities;

import jakarta.persistence.*;

import java.util.Random;

@Entity
@Table(
        name = "car_categories",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_categories_name", columnNames = "category_name")
        }
)
public class CarCategory {
    @Id
    @Column(name = "category_id", nullable = false, updatable = false)
    private Long categoryId;

    @Column(name = "category_name", nullable = false, unique = true, length = 100)
    private String categoryName;

    @Column(name = "category_description")
    private String categoryDescription;

    @PrePersist
    public void generateId(){
        if(this.categoryId == null){
            Random random = new Random();
            this.categoryId = 100000L + random.nextLong(900000);
        }

    }

    public CarCategory() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }
}
