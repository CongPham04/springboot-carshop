package com.carshop.oto_shop.repositories;

import com.carshop.oto_shop.entities.CarDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CarDetailRepository extends JpaRepository<CarDetail, Long> {
    Optional<CarDetail> findByCar_CarId(Long carId);
    @Modifying
    @Query("DELETE FROM CarDetail cd WHERE cd.car.carId = :carId")
    void deleteAllByCarId(@Param("carId") Long carId);
}
