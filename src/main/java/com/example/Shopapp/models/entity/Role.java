package com.example.Shopapp.models.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "roles")
@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
}
