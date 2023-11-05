package com.example.Shopapp.repositories;

import com.example.Shopapp.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Vì kế thưa JpaRepo => java tự hiểu đó là 1 repo => không cần thiết
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
