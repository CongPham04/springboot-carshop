package com.carshop.oto_shop.repositories;

import com.carshop.oto_shop.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarRepository extends JpaRepository<Car, Long> {
    @Modifying
    @Query("DELETE FROM Car c WHERE c.carCategory.categoryId = :categoryId")
    void deleteByCategoryId(@Param("categoryId") Long categoryId);
}
