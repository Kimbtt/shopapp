package com.example.Shopapp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Data

public class CategoryDto {
    @NotEmpty(message = "Khong được để trống trường này")
    private String name;
}
