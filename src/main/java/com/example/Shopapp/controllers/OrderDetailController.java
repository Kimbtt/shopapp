package com.example.Shopapp.controllers;

import com.example.Shopapp.exceptions.DataNotFoundException;
import com.example.Shopapp.models.dtos.OrderDetailDto;
import com.example.Shopapp.models.entity.OrderDetail;
import com.example.Shopapp.models.responses.OrderDetailResponse;
import com.example.Shopapp.models.responses.OrderResponse;
import com.example.Shopapp.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    private final ModelMapper modelMapper;
    // Thêm mới 1 order detail

    /**
     *
     * @param orderDetailDto
     * @param result
     * @return
     */
    @PostMapping("")
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
            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDto);
            // Tạo đối tượng orderDetailResponse trả về
            // Cách 2: sử dụng modelMapper
            // modelMapper.typeMap(OrderDetail.class, OrderDetailResponse.class);
            // OrderDetailResponse rs = new OrderDetailResponse();
            // modelMapper.map(newOrderDetail, rs);
            return ResponseEntity.ok(OrderDetailResponse.fromOderDetail(newOrderDetail));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @GetMapping("/{id}")
    public  ResponseEntity<?> getOrderDetail(
            @PathVariable Long id
    ) throws DataNotFoundException {
        try {
            OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
            return ResponseEntity.ok(orderDetail);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        }
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

        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        List<OrderDetailResponse> rs = orderDetails.stream().map(OrderDetailResponse::fromOderDetail).toList();
        return ResponseEntity.ok(rs);
    }

    /**
     * Sửa đổi 1 order detail
     * @param id
     * @param orderDetailDto
     * @return
     */
    @PutMapping("/{id}")
    public  ResponseEntity<?> updateOrderDetail(
            @PathVariable Long id,
            @RequestBody OrderDetailDto orderDetailDto
    ) {
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDto);
            return ResponseEntity.ok(orderDetail);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteOrderDetail(
            @PathVariable Long id
    ){
        orderDetailService.deleteById(id);
        return ResponseEntity.ok().body("Đã xóa");
//    return ResponseEntity.noContent().build();
    }
}
