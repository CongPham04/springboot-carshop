package com.carshop.oto_shop.repositories;

import com.carshop.oto_shop.entities.CarCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarCategoryRepository extends JpaRepository<CarCategory, Long> {
}
