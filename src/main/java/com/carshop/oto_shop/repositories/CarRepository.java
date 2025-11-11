package com.carshop.oto_shop.repositories;

import com.carshop.oto_shop.entities.Car;
import com.carshop.oto_shop.enums.Brand;
import com.carshop.oto_shop.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
// [THÊM MỚI] Import thư viện Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

// [SỬA ĐỔI] Thêm JpaSpecificationExecutor<Car>
public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {
    List<Car> findAllByCategory(Category category);
    List<Car> findAllByBrand(Brand brand);
}