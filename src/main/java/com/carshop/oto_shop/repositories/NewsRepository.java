package com.carshop.oto_shop.repositories;

import com.carshop.oto_shop.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
