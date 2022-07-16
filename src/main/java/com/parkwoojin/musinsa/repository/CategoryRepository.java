package com.parkwoojin.musinsa.repository;

import com.parkwoojin.musinsa.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByCodeAndDepth(String code, Integer depth);
}
