package com.example.Shopapp.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
// Không có Entity vì k phải là 1 entity (k có table)
public class BaseEntity {
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @PrePersist // Tự động cập nhật giá trị khi đăng ký
    protected void onCreate(){
        createAt = LocalDateTime.now();
        updateAt = LocalDateTime.now();
    }

    @PreUpdate // TỰ động cập nhật giá trị khi update
    protected void onUpdate(){
        updateAt = LocalDateTime.now();
    }
}
