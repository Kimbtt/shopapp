package com.example.Shopapp.controllers;
import com.example.Shopapp.dtos.OrderDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
//@Validated
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    @PostMapping("")
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody OrderDto orderDto,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            // Nếu không có lỗi, tiếp tục xử lý
            return ResponseEntity.ok("Create order successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getAllOrderByUserId(@PathVariable("user_id") Long userId){
        try {
            return ResponseEntity.ok("Danh sách orders từ user_id");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    //PUT http://localhost:8888/api/v1/orders/1
    // Công việc của admin
    public ResponseEntity<?> updateOrder(
            @PathVariable long id,
            @RequestBody OrderDto orderDto
    ){
        return ResponseEntity.ok("Cập nhật thông tin order");
    }
    @DeleteMapping("/{id}")
    //PUT http://localhost:8888/api/v1/orders/1
    // Công việc của admin
    public ResponseEntity<?> deleteOrder( @PathVariable Long id){
        // Xóa mềm => cập nhật trường active = false

        return ResponseEntity.ok("Xóa thông tin order");

    }

}
