package com.example.Shopapp.repositories;

import com.example.Shopapp.models.entity.Order;
import com.example.Shopapp.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Vì kế thưa JpaRepo => java tự hiểu đó là 1 repo => có hay k k quan trọng
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Tìm 1 đơn hàng của 1 user nào đó
    List<Order> findByUserId(Long userId);

    Optional<Order> findById(Long id);


}
