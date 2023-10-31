package com.example.Shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductDto {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 charaters")
    private String name;

    @Min(value = 0, message = "Discount must be greater than or equal to 0")
    @Max(value = 10000000, message = "Discount must be less than or equal to 10000000")
    private Float price;

    private String thumbnail;

    private String description;

    @JsonProperty("category_id")
    private String categoryId;

    private MultipartFile file;
}
