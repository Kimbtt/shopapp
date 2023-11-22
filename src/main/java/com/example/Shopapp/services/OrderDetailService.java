package com.example.Shopapp.services;

import com.example.Shopapp.exceptions.DataNotFoundException;
import com.example.Shopapp.models.dtos.OrderDetailDto;
import com.example.Shopapp.models.entity.OrderDetail;
import com.example.Shopapp.models.responses.OrderDetailResponse;

import java.util.List;

public interface OrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDto newOrderDetail) throws Exception;
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
    OrderDetail updateOrderDetail(Long id, OrderDetailDto newOrderDetailData)
            throws DataNotFoundException;
    void deleteById(Long id);
    List<OrderDetail> findByOrderId(Long orderId);
}
