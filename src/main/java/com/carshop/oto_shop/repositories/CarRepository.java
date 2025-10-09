package com.carshop.oto_shop.repositories;

import com.carshop.oto_shop.entities.Car;
import com.carshop.oto_shop.enums.Brand;
import com.carshop.oto_shop.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllByCategory(Category category);
    List<Car> findAllByBrand(Brand brand);
}
