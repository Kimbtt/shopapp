package com.example.Shopapp.services;

import com.example.Shopapp.exceptions.DataNotFoundException;
import com.example.Shopapp.models.dtos.OrderDto;
import com.example.Shopapp.models.entity.Order;
import com.example.Shopapp.models.responses.OrderResponse;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderDto orderDto) throws Exception;
    Order getOrder(Long id);
    Order updateOrder(Long id, OrderDto orderDto) throws DataNotFoundException;
    void deleteOrder(Long id);
    List<Order> findByUserId(Long userId);
}
