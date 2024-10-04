package com.example.library.repository;

import com.example.library.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DatabaseCategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);


}
