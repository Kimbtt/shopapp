package com.example.Shopapp.controller;

import com.example.Shopapp.dto.CategoryDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
//@Validated
@RequestMapping("api/v1/categories")
public class CategoryController {
    // Hiển thị tất cả các category
    @GetMapping("") //http://localhost:8888/api/v1/categories?page=1&limit=10
    public ResponseEntity<String> getAllCategories(
            @RequestParam("page") int page, //page trong RequestParam dùng để ánh xạ lên frontend
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok(String.format("get all categories, page: %d, limit: %d", page, limit));
    }

    /**
     *  Tạo mới 1 category
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

        // Nếu không có lỗi, tiếp tục xử lý
        return ResponseEntity.ok("Đã tạo 1 bảng ghi mới: " + categoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable("id") int id) {
        return ResponseEntity.ok("Đã sửa bảng ghi " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") int id) {
        return ResponseEntity.ok("Đã xóa bảng ghi " + id);
    }
}
