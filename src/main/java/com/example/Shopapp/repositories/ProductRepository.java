package com.example.Shopapp.repositories;

import com.example.Shopapp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository // Vì kế thưa JpaRepo => java tự hiểu đó là 1 repo => có hay k k quan trọng
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

//    Page<Product> findAll(Pageable pageable); // Dùng để phân trang
}
