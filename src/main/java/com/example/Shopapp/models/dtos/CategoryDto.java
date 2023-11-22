package com.example.Shopapp.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Data

public class CategoryDto {
    @NotEmpty(message = "Khong được để trống trường này")
    private String name;
}
