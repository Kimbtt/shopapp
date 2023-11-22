package com.example.Shopapp.controllers;
import com.example.Shopapp.models.dtos.OrderDto;
import com.example.Shopapp.models.entity.Order;
import com.example.Shopapp.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping("")
    public ResponseEntity<?> createOrder(
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
            Order orderResponse = orderService.createOrder(orderDto);

            // Nếu không có lỗi, tiếp tục xử lý
            return ResponseEntity.ok(orderResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{user_id}") // Thêm biến đường dẫn "user_id"
    //GET http://localhost:8088/api/v1/orders/user/4
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId) {
        try {
            List<Order> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    //GET http://localhost:8888/api/v1/orders/1
    public ResponseEntity<?> getOrder(
            @PathVariable Long id
    ){
        try {
            Order existingOrder = orderService.getOrder(id);
            return ResponseEntity.ok(existingOrder);

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
        try {
            Order existingOrder = orderService.updateOrder(id, orderDto);
            return ResponseEntity.ok(existingOrder);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    //PUT http://localhost:8888/api/v1/orders/1
    // Công việc của admin
    public ResponseEntity<?> deleteOrder( @PathVariable Long id){
        // Xóa mềm => cập nhật trường active = false
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Xóa thông tin order");

    }

}
