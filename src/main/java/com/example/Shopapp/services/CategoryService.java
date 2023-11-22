package com.example.Shopapp.services;

import com.example.Shopapp.models.dtos.CategoryDto;
import com.example.Shopapp.models.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryDto categoryDTO);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(Long id, CategoryDto categoryDTO);
    void deleteCategory(Long id);
}
