package com.example.Shopapp.controllers;

import com.example.Shopapp.dtos.OrderDetailDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {

    // Thêm mới 1 order detail

    /**
     *
     * @param orderDetailDto
     * @param result
     * @return
     */
    @PostMapping
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDto orderDetailDto,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()){
                List<String> erroMess = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroMess);
            }

            return ResponseEntity.ok("Đã đăng ký 1 orderdetail");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @GetMapping("/{id}")
    public  ResponseEntity<?> getOrderDetail(
            @PathVariable Long id
    ){
    return ResponseEntity.ok("Get Order detail Id: " + id);
    }

    /**
     * Lấy danh sách order detail của 1 cái order nào đó
     * @param orderId
     * @return
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(
        @Valid @PathVariable("orderId") Long orderId
    ){
        return ResponseEntity.ok("Get Order details with orderId: " + orderId);
    }

    /**
     * Sửa đổi 1 order detail
     * @param id
     * @param newOrderDetailData
     * @return
     */
    @PutMapping("/{id}")
    public  ResponseEntity<?> updateOrderDetail(
            @PathVariable Long id,
            @RequestBody OrderDetailDto newOrderDetailData
    ){
        return ResponseEntity.ok("Update Order detail Id: " + id + " with newOrderDetailData:" + newOrderDetailData);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteOrderDetail(
            @PathVariable Long id
    ){
    return ResponseEntity.noContent().build();
    }
}
