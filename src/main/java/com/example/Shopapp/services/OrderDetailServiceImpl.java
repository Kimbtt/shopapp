package com.example.Shopapp.services;

import com.example.Shopapp.exceptions.DataNotFoundException;
import com.example.Shopapp.models.dtos.OrderDetailDto;
import com.example.Shopapp.models.entity.Order;
import com.example.Shopapp.models.entity.OrderDetail;
import com.example.Shopapp.models.entity.Product;
import com.example.Shopapp.models.responses.OrderDetailResponse;
import com.example.Shopapp.models.responses.OrderResponse;
import com.example.Shopapp.repositories.OrderDetailRepository;
import com.example.Shopapp.repositories.OrderRepository;
import com.example.Shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService{
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    @Override
    public OrderDetail createOrderDetail(OrderDetailDto newOrderDetailDto) throws Exception {
        // Tìm xem order có tồn tại khong
        Order order = orderRepository.findById(newOrderDetailDto.getOrderId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find Order with id : "+newOrderDetailDto.getOrderId()));
        // Tìm Product theo id
        Product product = productRepository.findById(newOrderDetailDto.getProductId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id: " + newOrderDetailDto.getProductId()));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(newOrderDetailDto.getNumberOfProducts())
                .price(newOrderDetailDto.getPrice())
                .totalMoney(newOrderDetailDto.getTotalMoney())
                .color(newOrderDetailDto.getColor())
                .build();

        //lưu vào db
        orderDetailRepository.save(orderDetail);

        return orderDetail;
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find OrderDetail with id: " + id));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDto orderDetailDto) throws DataNotFoundException {
        // Tìm orderDetail
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Không có thông tin order detail với id123: " + id));
        Order existingOrder = orderRepository.findById(orderDetailDto.getOrderId())
                .orElseThrow(()-> new DataNotFoundException(
                        "Không tìm thấy thông tin order với id: " + orderDetailDto.getOrderId()));
        Product existingProduct = productRepository.findById(orderDetailDto.getProductId())
                .orElseThrow(()->new DataNotFoundException(
                        "Cannot find product with id: " + orderDetailDto.getProductId()
                ));
        // update
        existingOrderDetail.setPrice(orderDetailDto.getPrice());
        existingOrderDetail.setTotalMoney(orderDetailDto.getTotalMoney());
        existingOrderDetail.setNumberOfProducts(orderDetailDto.getNumberOfProducts());
        existingOrderDetail.setColor(orderDetailDto.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
