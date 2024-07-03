package com.example.bake_boss_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bake_boss_backend.entity.CategoryName;

public interface CategoryNameRepository extends JpaRepository<CategoryName, Long>{

    boolean existsByUsernameAndCategoryName(String username, String categoryName);

    List<CategoryName> getCategoryNameByUsername(String username);
    
}
