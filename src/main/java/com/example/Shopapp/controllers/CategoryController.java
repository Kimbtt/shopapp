package com.example.Shopapp.controllers;

import com.example.Shopapp.dtos.CategoryDto;
import com.example.Shopapp.models.Category;
import com.example.Shopapp.services.CategoryServiceImpl;
import com.example.Shopapp.services.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;


@RestController
//@Validated
//Dependency Injection
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final CategoryServiceImpl categoryServiceImpl;

    /**
     * Tạo mới 1 category
     *
     * @param categoryDTO
     * @param result
     * @return
     */
    @PostMapping("")
    // Nếu tham số truyền vào là 1 object thì sao? => Phải transfer -> Data Transfer Object -> Request Data
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDto categoryDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }

        categoryServiceImpl.createCategory(categoryDTO);
        // Nếu không có lỗi, tiếp tục xử lý
        return ResponseEntity.ok("Insert Category successfully");
    }

    // Hiển thị tất cả các category
    @GetMapping("") //http://localhost:8888/api/v1/categories?page=1&limit=10
    public ResponseEntity<?> getAllCategories(
            @RequestParam("page") int page, //page trong RequestParam dùng để ánh xạ lên frontend
            @RequestParam("limit") int limit
    ) {
        // Lấy danh sách categories
        List<Category> categories = categoryServiceImpl.getAllCategories();


        return ResponseEntity.ok(categories);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable("id") Long id,
            @RequestBody CategoryDto categoryDto
    ) {
        Category category = categoryServiceImpl.updateCategory(id ,categoryDto);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long id) {
        categoryServiceImpl.deleteCategory(id);
        return ResponseEntity.ok("Đã xóa bảng ghi " + id);
    }
}
