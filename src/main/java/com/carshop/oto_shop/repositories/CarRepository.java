package com.carshop.oto_shop.repositories;

import com.carshop.oto_shop.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllByCarCategory_CategoryId(Long categoryId);
    List<Car> findAllByCarBrand_BrandId(Long brandId);
}
