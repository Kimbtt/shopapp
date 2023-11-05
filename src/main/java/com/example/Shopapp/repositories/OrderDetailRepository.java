package com.example.Shopapp.repositories;

import com.example.Shopapp.models.Category;
import com.example.Shopapp.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository // Vì kế thưa JpaRepo => java tự hiểu đó là 1 repo => có hay k k quan trọng
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}
