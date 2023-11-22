package com.example.Shopapp.services;

import com.example.Shopapp.models.dtos.CategoryDto;
import com.example.Shopapp.models.entity.Category;
import com.example.Shopapp.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // Sẽ tạo constructor cho nhưững thèng có final
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    // Hàm tạo được lược bỏ vì có @RequiredArgsContructor
//    public CategoryServiceImpl(CategoryRepository categoryRepository){
//        this.categoryRepository = categoryRepository;
//    }
    @Override
    public Category createCategory(CategoryDto categoryDTO) {
        Category newCategory = Category
                .builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(Long categoryId, CategoryDto categoryDTO) {
        Category existingCategory = getCategoryById(categoryId);
        existingCategory.setName(categoryDTO.getName());
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    public void deleteCategory(Long id) {
        // Xóa cứng
        categoryRepository.deleteById(id);
    }
}
