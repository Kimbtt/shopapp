package com.example.Shopapp.models.dtos;

import com.example.Shopapp.models.entity.OrderDetail;
import com.example.Shopapp.models.responses.OrderDetailResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class OrderDetailDto {
    @JsonProperty("order_id")
    @Min(value = 1, message = "Order's id must be > 0")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's id must be > 0")
    private Long productId;

    @Min(value = 0, message = "Price must be >= 0")
    private Float price;

    @JsonProperty("number_of_products")
    @Min(value = 1, message = "Number_of_products must be > 0")
    private Integer numberOfProducts;

    @Min(value = 0, message = "Price must be >= 0")
    @JsonProperty("total_money")
    private Float totalMoney;

    private String color;


}
