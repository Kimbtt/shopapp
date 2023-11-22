package com.example.Shopapp.repositories;

import com.example.Shopapp.models.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Vì kế thưa JpaRepo => java tự hiểu đó là 1 repo => có hay k k quan trọng
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Long productId);
//    List<ProductImage> findAllByProduct(Product productId);
}
