package com.example.Shopapp.repositories;

import com.example.Shopapp.models.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//@Repository // Vì kế thưa JpaRepo => java tự hiểu đó là 1 repo => có hay k k quan trọng
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId(Long orderId);

}
